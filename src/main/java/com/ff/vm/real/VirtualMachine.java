package com.ff.vm.real;

import com.ff.vm.real.builtin.*;
import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.*;
import com.ff.vm.real.type.constant.BasicConstant;
import com.ff.vm.real.util.BuiltIn;
import com.ff.vm.real.util.CompareOperator;
import com.ff.vm.tools.DisTools;
import com.ff.vm.tools.PycReader;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Triplet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static com.ff.vm.real.VirtualMachineStatic.*;

/**
 * Created by chjun1991@163.com on 2018/5/18.
 * https://docs.python.org/2/library/dis.html
 * https://docs.python.org/2.0/ref/execframes.html
 * closure https://www.cnblogs.com/ChrisChen3121/p/3208119.html
 */

@Slf4j
public class VirtualMachine {


    //contain pyc file to load
    public String libDir;

    //frame stack
    private Deque<Frame> frameStack = new ArrayDeque<>();

    //current frame
    private Frame  frame = null;


    private PyObject return_value = null;

    private static Map<String,Method> opMethodMap = new HashMap<>();

    private static Method [] fastArray = new Method[256];


    public  static Map<PyStr,PyObject> builtInConstants = new HashMap<>();

    //wheather interactive mode
    private boolean isInInteractiveMode = false;

    static {
        //https://docs.python.org/2.7/library/constants.html
        builtInConstants.put(new PyStr("False"),BasicConstant.TYPE_FALSE);
        builtInConstants.put(new PyStr("True"),BasicConstant.TYPE_TRUE);
        builtInConstants.put(new PyStr("None"), BasicConstant.TYPE_NONE);
        builtInConstants.put(new PyStr("NotImplemented"), BasicConstant.TYPE_NOT_IMPLEMENT);
        builtInConstants.put(new PyStr("Ellipsis"), BasicConstant.TYPE_ELLIPSIS);
        builtInConstants.put(new PyStr("__debug__"), BasicConstant.TYPE_FALSE);


        //put builtIn function
        builtInConstants.put(new PyStr("range"),new Range());
        builtInConstants.put(new PyStr("Exception"),new BaseException(Collections.EMPTY_LIST));
        builtInConstants.put(new PyStr("UserWarning"),new UserWarning(Collections.EMPTY_LIST));
        builtInConstants.put(new PyStr("VmException"),new VmException(Collections.EMPTY_LIST));
        builtInConstants.put(new PyStr("len"),new Len());
    }

    static {
        for(Method m:VirtualMachine.class.getDeclaredMethods()){
            if(m.getName().startsWith("OP_")){
                opMethodMap.put(m.getName().substring(3),m);
            }
        }

        for(int i=0;i<op.length;i++){
            if(op[i]!=null){
                fastArray[i]=opMethodMap.get(op[i]);
            }
        }
    }

    public VirtualMachine(String libDir) {
        this.libDir = libDir;
    }

    public void runFile(String fileName){
        PycReader reader = new PycReader();
        try {
            Code code =  reader.readFile(libDir+"/"+fileName);
            run_code(code);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public void run_code(Code code){
        log.info(DisTools.dis(code));
        Map<PyStr,PyObject> global= new HashMap<>();
        global.put(new PyStr("__name__"),new PyStr("__main__"));

        Frame frame = new Frame(code, global,Collections.EMPTY_MAP,builtInConstants,null,null);
        push_frame(frame);
        __run_frame(frame);
    }

    public void import_run_frame(Frame frame){
        push_frame(frame);
    }

    public void __run_frame(Frame frame){

        while (true){

            Triplet<String,PyObject,Integer> op = parse_byte_arg();

            Object why = null;
            try {
                why = dispatch(op);
            } catch (RuntimeException e) {
                log.error(e.toString());
                raiseVmException("VM exception:"+e.getMessage());
                why = Why.EXCEPTION;
            }

            //return;
            if(why!=null) {
                Why w = (Why) why;
                switch (w) {
                    case RETURN:
                        break;
                    case EXCEPTION: {
                        while (true) {
                            BaseException e = (BaseException) curFrame().stack.pop();
                            if(frameStack.size()>1) {
                                pop_frame();
                                curFrame().stack.push(e);
                                Object ret = OP_RAISE_VARARGS(new PyInt(1));
                                if (ret != null) {
                                    //cur frame can not produce exception
                                    continue;
                                } else {
                                    break;
                                }
                            }else{
                                //no frame to pop,print exceptoion detail
                                System.out.println(e.__str__().toString());
                                //terminated vm
                                return_value =  BasicConstant.TYPE_NONE;
                                return;
                            }
                        }
                    }
                    break;
                    default:
                        break;
                }
                break;
            }
        }

    }

    public Frame curFrame(){
        return frame;
    }

    public void push_frame(Frame frame){
        frameStack.push(frame);
        this.frame = frame;
    }

    public Frame pop_frame(){
        Frame ret = frameStack.pollFirst();//equal pop ,but not throw exception.
        frame = frameStack.peekLast();
        return ret;
    }

    private Triplet<String,PyObject,Integer> parse_byte_arg(){

        Frame f = frame;

        int b = 0xff & f.code.co_code.value[f.next_instruction++];

        PyObject argObj = null;

        if(b>=HAS_ARGUMENT){
            int arg = (short) (( 0xff & f.code.co_code.value[f.next_instruction]) + ((0xff & f.code.co_code.value[f.next_instruction+1])<<8));
            arg += (0xffff & f.prefix_op_arg)<<16;
            f.prefix_op_arg = 0 ;
            if(hasconst.contains((int)b)){
                argObj = f.code.co_consts.value[arg];
            }else if(hasfree.contains(b)){

                if(arg<f.code.co_cellvars.value.length){
                    argObj = f.code.co_cellvars.value[arg];
                }else{
                    int var_idx = arg - f.code.co_cellvars.value.length;
                    argObj = f.code.co_freevars.value[var_idx];
                }
            }else if(hasname.contains(b)){
                argObj = f.code.co_names.value[arg];
            }else if(hasjrel.contains(b)){
                argObj =  new PyInt(arg);
            }else if(hasjabs.contains(b)){
                argObj = new PyInt(arg);
            }else if(haslocal.contains(b)){
                argObj = f.code.co_varnames.value[arg];
            }else{
                argObj = new PyInt(arg);
            }

            f.next_instruction+=2;
        }

        Triplet<String,PyObject,Integer> op = new Triplet(opcodeTostr(b),argObj,b);

        return op;
    }


    private Object dispatch(Triplet<String,PyObject,Integer> op){

        String opCode = op.getValue0();
        PyObject arg = op.getValue1();


        Method m = fastArray[op.getValue2()];
        if(m==null){
            throw new RuntimeException("unsupported instruct："+opCode+" "+op.getValue2());
        }
        try {
            if(arg!=null)
                 return m.invoke(this,arg);
            else
                 return m.invoke(this);
        } catch (RuntimeException e) {
           throw new RuntimeException(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (InvocationTargetException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getTargetException().getMessage());
        }
    }

    public void raiseVmException(String msg){
        BuiltInFunction func = (BuiltInFunction) builtInConstants.get(new PyStr("VmException"));

        List args = new ArrayList();
        args.add(new PyStr(msg));

        frame.stack.push(func.call(this,args,new PyDict()));
        this.OP_RAISE_VARARGS(new PyInt(1));
    }


    // see https://docs.python.org/2/library/dis.html#python-bytecode-instructions
    private void OP_STOP_CODE(){

    }

    private void OP_NOP(){

    }

    private void OP_POP_TOP(){
        frame.stack.pop();
    }

    private void OP_ROT_TWO(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        frame.stack.push(obj0);
        frame.stack.push(obj1);
    }

    private void OP_ROT_THREE(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        PyObject obj2= frame.stack.pop();
        frame.stack.push(obj0);
        frame.stack.push(obj2);
        frame.stack.push(obj1);
    }

    private void OP_ROT_FOUR(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        PyObject obj2= frame.stack.pop();
        PyObject obj3= frame.stack.pop();
        frame.stack.push(obj0);
        frame.stack.push(obj3);
        frame.stack.push(obj2);
        frame.stack.push(obj1);
    }

    private void OP_DUP_TOP(){
        frame.stack.push(frame.stack.peek());
    }

    //unchanged
    private void OP_UNARY_POSITIVE(){
        PyInt obj = (PyInt) frame.stack.pop();
        PyInt obj2 = obj;
        frame.stack.push(obj2);
    }

    private void OP_UNARY_NEGATIVE(){
        PyInt obj = (PyInt) frame.stack.pop();
        frame.stack.push(new PyInt(0-obj.value));
    }

    private void OP_UNARY_NOT(){
        PyBool obj = (PyBool) frame.stack.pop();
        frame.stack.push(new PyBool(!obj.value));
    }

    private void OP_UNARY_CONVERT(){
        PyObject obj = frame.stack.pop();
        PyStr obj2 = obj.__str__();
        frame.stack.push(obj2);
    }

    private void OP_UNARY_INVERT(){
        PyObject obj = frame.stack.pop();
        PyObject obj2 = obj.__unary_invert__();
        frame.stack.push(obj2);
    }

    private void OP_GET_ITER(){
        PyObject obj = frame.stack.pop();
        PyIterator ite = obj.__iter__();
        frame.stack.push(ite);
    }

    private void OP_BINARY_POWER(){
        PyObject obj0 =  frame.stack.pop();
        PyObject obj1 =  frame.stack.pop();
        PyObject d = obj1.__pow__(obj0);
        frame.stack.push(d);
    }

    private void OP_BINARY_MULTIPLY(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        PyObject obj3 = obj1.__mul__(obj0);
        frame.stack.push(obj3);
    }

    private void OP_BINARY_DIVIDE(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        PyObject obj3 = obj1.__div__(obj0);
        frame.stack.push(obj3);
    }

    private void OP_BINARY_FLOOR_DIVIDE(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        PyObject obj3 = obj1.__floordiv__(obj0);
        frame.stack.push(obj3);
    }


    private void OP_BINARY_TRUE_DIVIDE(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        PyObject obj3 = obj1.__truediv__(obj0);
        frame.stack.push(obj3);
    }

    private void OP_BINARY_MODULO(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        PyObject obj3 = obj1.__mod__(obj0);
        frame.stack.push(obj3);
    }


    private void OP_BINARY_ADD(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 = obj1.__add__(obj0);
        frame.stack.push(obj3);
    }


    private void OP_BINARY_SUBTRACT(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__sub__(obj0);
        frame.stack.push(obj3);
    }

    public void OP_BINARY_SUBSCR(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__subscr__(obj0);
        frame.stack.push(obj3);
    }

    public void OP_BINARY_LSHIFT(){

        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__lshift__(obj0);
        frame.stack.push(obj3);
    }

    public void OP_BINARY_RSHIFT(){

        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__rshift__(obj0);
        frame.stack.push(obj3);
    }

    public void OP_BINARY_AND(){

        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__and__(obj0);
        frame.stack.push(obj3);

    }

    public void OP_BINARY_XOR(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__xor__(obj0);
        frame.stack.push(obj3);
    }


    private void OP_BINARY_OR(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__or__(obj0);
        frame.stack.push(obj3);
    }


    //Implements in-place TOS = TOS1 ** TOS.
    public void OP_INPLACE_POWER(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__ipow__(obj0);
        frame.stack.push(obj3);
    }

    //Implements in-place TOS = TOS1 * TOS.
    public void OP_INPLACE_MULTIPLY(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__imul__(obj0);
        frame.stack.push(obj3);
    }

    //Implements in-place TOS = TOS1 / TOS when from __future__ import division is not in effect.
    public void OP_INPLACE_DIVIDE(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__idiv__(obj0);
        frame.stack.push(obj3);
    }

    //Implements in-place TOS = TOS1 // TOS.
    public void OP_INPLACE_FLOOR_DIVIDE(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__ifloordiv__(obj0);
        frame.stack.push(obj3);
    }

    //Implements in-place TOS = TOS1 / TOS when from __future__ import division is in effect.
    public void OP_INPLACE_TRUE_DIVIDE(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__itruediv__(obj0);
        frame.stack.push(obj3);
    }

    //Implements in-place TOS = TOS1 % TOS.
    public void OP_INPLACE_MODULO(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__imod__(obj0);
        frame.stack.push(obj3);
    }

    //Implements in-place TOS = TOS1 + TOS.
    public void OP_INPLACE_ADD(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__iadd__(obj0);
        frame.stack.push(obj3);
    }

    //Implements in-place TOS = TOS1 - TOS.
    public void OP_INPLACE_SUBTRACT(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__isub__(obj0);
        frame.stack.push(obj3);
    }

    //Implements in-place TOS = TOS1 << TOS.
    public void OP_INPLACE_LSHIFT(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__ilshift__(obj0);
        frame.stack.push(obj3);
    }

    //Implements in-place TOS = TOS1 >> TOS.
    public void OP_INPLACE_RSHIFT(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__irshift__(obj0);
        frame.stack.push(obj3);
    }

    //Implements in-place TOS = TOS1 & TOS.
    public void OP_INPLACE_AND(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__iand__(obj0);
        frame.stack.push(obj3);
    }

    //Implements in-place TOS = TOS1 ^ TOS.
    public void OP_INPLACE_XOR(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__ixor__(obj0);
        frame.stack.push(obj3);
    }

    //Implements in-place TOS = TOS1 | TOS.
    public void OP_INPLACE_OR(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__ior__(obj0);
        frame.stack.push(obj3);
    }

    //The slice opcodes take up to three parameters.

    //Implements TOS = TOS[:].
    public void OP_SLICE_0(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__slice0__();
        frame.stack.push(obj3);
    }

    //Implements TOS = TOS1[TOS:].
    public void OP_SLICE_1(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__slice1__(obj0);
        frame.stack.push(obj3);
    }

    //Implements TOS = TOS1[:TOS].
    public void OP_SLICE_2(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj1.__slice2__(obj0);
        frame.stack.push(obj3);
    }

    //Implements TOS = TOS2[TOS1:TOS].
    public void OP_SLICE_3(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        PyObject obj2 = frame.stack.pop();
        //the implement is wrong ,just support int
        PyObject obj3 =obj2.__slice3__(obj1,obj0);
        frame.stack.push(obj3);
    }

    //Slice assignment needs even an additional parameter. As any statement, they put nothing on the stack.

    //Implements TOS[:] = TOS1.
    public void OP_STORE_SLICE_0(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        frame.stack.push(obj1);
    }

    //Implements TOS1[TOS:] = TOS2.
    public void OP_STORE_SLICE_1(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        PyObject obj2 = frame.stack.pop();
        PyObject obj3 =obj1.__store_slice1__(obj0,obj2);
        frame.stack.push(obj1);
    }

    //Implements TOS1[:TOS] = TOS2.
    public void OP_STORE_SLICE_2(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        PyObject obj2 = frame.stack.pop();
        PyObject obj3 =obj1.__store_slice2__(obj0,obj2);
        frame.stack.push(obj1);
    }

    //Implements TOS2[TOS1:TOS] = TOS3.
    public void OP_STORE_SLICE_3(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        PyObject obj2 = frame.stack.pop();
        PyObject obj3 = frame.stack.pop();
        PyObject obj4 =obj2.__store_slice3__(obj1,obj0,obj3);
        frame.stack.push(obj4);
    }

    //Implements del TOS[:].
    public void OP_DELETE_SLICE_0(){
        PyObject obj0 = frame.stack.pop();
        obj0.__delslice__();
        frame.stack.push(obj0);
    }

    //Implements del TOS1[TOS:].
    public void OP_DELETE_SLICE_1(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        obj1.__delslice1__(obj0);
        frame.stack.push(obj1);
    }

    //Implements del TOS1[:TOS].
    public void OP_DELETE_SLICE_2(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        obj1.__delslice2__(obj0);
        frame.stack.push(obj1);
    }

    //Implements del TOS2[TOS1:TOS].
    public void OP_DELETE_SLICE_3(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        PyObject obj2 = frame.stack.pop();
        obj2.__delslice3__(obj1,obj0);
        frame.stack.push(obj1);
    }

    //Implements TOS1[TOS] = TOS2.
    public void OP_STORE_SUBSCR(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        PyObject obj2 = frame.stack.pop();
        obj1.__storesubscr__(obj0,obj2);
    }

    //Implements del TOS1[TOS].
    public void OP_DELETE_SUBSCR(){
        PyObject obj0 = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        obj1.__delsubscr__(obj0);
        frame.stack.push(obj1);
    }

    //Implements the expression statement for the interactive mode. TOS is removed from the stack and printed. In non-interactive mode, an expression statement is terminated with POP_TOP.
    public void OP_PRINT_EXPR(){
        PyObject obj0 = frame.stack.pop();
        if(isInInteractiveMode)
            System.out.print(new String(obj0.__str__().value));
    }


    private void OP_PRINT_ITEM(){
        PyObject obj = frame.stack.pop();
        System.out.print(new String(obj.__str__().value));
        System.out.print(" ");
    }

    //Like PRINT_ITEM, but prints the item second from TOS to the file-like object at TOS. This is used by the extended print statement.
    public void OP_PRINT_ITEM_TO(){
        PyObject to = frame.stack.pop();
        PyObject item = frame.stack.pop();
        to.__print__(item);
    }

    private void OP_PRINT_NEWLINE(){
        System.out.println();
    }


    //Like PRINT_NEWLINE, but prints the new line on the file-like object on the TOS. This is used by the extended print statement.
    public void OP_PRINT_NEWLINE_TO(){
        PyObject to = frame.stack.pop();
        to.__print__(new PyStr(""));
    }

    //Terminates a loop due to a break statement.
    public void OP_BREAK_LOOP(){
        Block block = frame.blocks.peek();
        frame.next_instruction = block.toAddress;
    }

    //Continues a loop due to a continue statement. target is the address to jump to (which should be a FOR_ITER instruction).
    public void OP_CONTINUE_LOOP(PyInt target){
        frame.next_instruction = (int) target.value;
    }

    //Calls list.append(TOS[-i], TOS). Used to implement list comprehensions.
    // While the appended value is popped off,
    // the list object remains on the stack so that it is available for further iterations of the loop.
    public void OP_LIST_APPEND(PyInt i){
        PyObject val = frame.stack.pop();
        PyList list = (PyList) frame.stack.get((int) (frame.stack.size()-i.value));
        list.append(val);
    }

    //Pushes a reference to the locals of the current scope on the stack.
    // This is used in the code for a class definition: After the class body is evaluated,
    // the locals are passed to the class definition.
    public void OP_LOAD_LOCALS(){
        frame.stack.push(new PyDict(frame.local_names));
    }

    //Returns with TOS to the caller of the function.
    public Object OP_RETURN_VALUE(){
        PyObject obj = frame.stack.pop();
        return_value = obj;
        return Why.RETURN;
    }

    //Pops TOS and yields it from a generator.
    public Object OP_YIELD_VALUE(){
        PyObject obj = frame.stack.pop();
        return_value = obj;
        return Why.YIELD;
    }

    //Loads all symbols not starting with '_' directly from the module TOS to the local namespace.
    // The module is popped after loading all names. This opcode implements from module import *.
    public void OP_IMPORT_STAR(){
        PyObject mod = frame.stack.pop();

    }

    //Implements exec TOS2,TOS1,TOS. The compiler fills missing optional parameters with None.
    public void OP_EXEC_STMT(){
    }

    //Removes one block from the block stack. Per frame,
    // there is a stack of blocks, denoting nested loops, try statements, and such.
    public void OP_POP_BLOCK(){
        frame.blocks.pop();
    }

    //Terminates a finally clause.
    // The interpreter recalls whether the exception has to be re-raised,
    // or whether the function returns, and continues with the outer-next block.
    public void OP_END_FINALLY(){

    }

    //Creates a new class object.
    // TOS is the methods dictionary,
    // TOS1 the tuple of the names of the base classes,
    // and TOS2 the class name.
    public void OP_BUILD_CLASS(){

        PyDict local = (PyDict) frame.stack.pop();
        PyTuple base = (PyTuple) frame.stack.pop();
        PyStr name = (PyStr) frame.stack.pop();

        PyClass pyClass = new PyClass(name,base,local);

        frame.stack.push(pyClass);
    }

    //This opcode performs several operations before a with block starts.
    // First, it loads __exit__() from the context manager and pushes it onto the stack for later use by WITH_CLEANUP.
    // Then, __enter__() is called, and a finally block pointing to delta is pushed. Finally,
    // the result of calling the enter method is pushed onto the stack.
    // The next opcode will either ignore it (POP_TOP), or store it in (a) variable(s) (STORE_FAST, STORE_NAME, or UNPACK_SEQUENCE).
    public void OP_SETUP_WITH(PyObject delta){
    }

    //Cleans up the stack when a with statement block exits.
    // On top of the stack are 1–3 values indicating how/why the finally clause was entered:
    public void OP_WITH_CLEANUP(){
    }


    //Implements name = TOS.
    // namei is the index of name in the attribute co_names of the code object.
    // The compiler tries to use STORE_FAST or STORE_GLOBAL if possible.
    private void OP_STORE_NAME(PyStr name){
        PyObject obj = frame.stack.pop();
        frame.local_names.put(name,obj);
    }

    //Implements del name, where namei is the index into co_names attribute of the code object.
    public void OP_DELETE_NAME(PyStr name){
        frame.local_names.remove(name);

    }

    //Unpacks TOS into count individual values, which are put onto the stack right-to-left.
    public void OP_UNPACK_SEQUENCE(PyObject count){
        PyObject top = frame.stack.pop();

        throw new RuntimeException("not implement");

    }

    //Duplicate count items, keeping them in the same order. Due to implementation limits, count should be between 1 and 5 inclusive.
    public void OP_DUP_TOPX(PyInt count){
        PyObject top = frame.stack.peek();
        for(int i=0;i<count.value;i++){
            frame.stack.push(top);
        }
    }

    //Implements TOS.name = TOS1, where namei is the index of name in co_names.
    public void OP_STORE_ATTR(PyStr name){
        PyObject tos = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        tos.__store_attr__(name,obj1);

    }

    //Implements del TOS.name, using namei as index into co_names.
    public void OP_DELETE_ATTR(PyStr name){
        PyObject tos = frame.stack.pop();
        PyObject obj1 = frame.stack.pop();
        tos.__del_attr__(name,obj1);
    }

    //Works as STORE_NAME, but stores the name as a global.
    public void OP_STORE_GLOBAL(PyStr name){
        PyObject obj = frame.stack.pop();
        frame.global_names.put(name,obj);
    }

    //Works as DELETE_NAME, but deletes a global name.
    public void OP_DELETE_GLOBAL(PyObject name){
        frame.global_names.remove(name);
    }

    //Pushes co_consts[consti] onto the stack.
    public void OP_LOAD_CONST(PyObject constObj){
        frame.stack.push(constObj);
    }

    //Pushes the value associated with co_names[namei] onto the stack.
    private void OP_LOAD_NAME(PyStr name){
        PyObject obj = null;

        obj = frame.local_names.get(name);
        if(obj!=null) {
            frame.stack.push(obj);
            return;
        }

        obj = frame.global_names.get(name);
        if(obj!=null) {
            frame.stack.push(obj);
            return;
        }

        obj = frame.builtIn.get(name);
        if(obj!=null){
            frame.stack.push(obj);
            return;
        }

        throw new RuntimeException("do not have name " + name);
    }

    //Creates a tuple consuming count items from the stack, and pushes the resulting tuple onto the stack.
    public void OP_BUILD_TUPLE(PyInt count){

        PyObject [] arr = new PyObject[(int) count.value];

        for(int i = (int) (count.value-1); i>=0; i--){
            arr[i] = frame.stack.pop();
        }

        PyTuple tuple = new PyTuple(arr);
        frame.stack.push(tuple);
    }

    //Works as BUILD_TUPLE, but creates a list.
    public void OP_BUILD_LIST(PyInt count){
        List<PyObject>  arr = new ArrayList<>((int) count.value);

        for(int i=0;i<count.value;i++){
            arr.add(null);
        }

        for(int i = (int) (count.value-1); i>=0; i--){
            arr.set(i,frame.stack.pop());
        }
        PyList list = new PyList(arr);
        frame.stack.push(list);
    }

    //Works as BUILD_TUPLE, but creates a set.
    public void OP_BUILD_SET(PyInt count){
        PyObject [] arr = new PyObject[(int) count.value];

        for(int i=0;i<arr.length;i++){
            arr[i] = frame.stack.pop();
        }

        PySet set = new PySet(arr);
        frame.stack.push(set);
    }

    //New in version 2.7.

    //Pushes a new dictionary object onto the stack. The dictionary is pre-sized to hold count entries.
    public void OP_BUILD_MAP(PyInt count){
        frame.stack.push(new PyDict(count));
    }

    //Replaces TOS with getattr(TOS, co_names[namei]).
    public void OP_LOAD_ATTR(PyStr name){
        PyObject tos = frame.stack.pop();
        PyObject attr = tos.__attr__(name);
        frame.stack.push(attr);
    }

    //Performs a Boolean operation. The operation name can be found in cmp_op[opname].
    public void OP_COMPARE_OP(PyInt opname){
        PyObject op1 = frame.stack.pop();
        PyObject op2 = frame.stack.pop();
        frame.stack.push(CompareOperator.op(opname,op2,op1));

    }

    //Imports the module co_names[namei]. TOS and TOS1 are popped and provide the fromlist and level arguments of __import__().
    //
    // The module object is pushed onto the stack. The current namespace is not affected:
    // for a proper import statement, a subsequent STORE_FAST instruction modifies the namespace.
    public void OP_IMPORT_NAME(PyStr name){
        PyObject fromlist = frame.stack.pop();
        PyObject  level = frame.stack.pop();

        PyModule module = BuiltIn.__import__(this,name,frame.global_names,frame.local_names,level,fromlist);

        frame.stack.push(module);
    }

    //Loads the attribute co_names[namei] from the module found in TOS.
    // The resulting object is pushed onto the stack, to be subsequently stored by a STORE_FAST instruction.
    public void OP_IMPORT_FROM(PyStr name){
        PyModule module = (PyModule) frame.stack.pop();
        PyObject aa = module.__attr__(name);

        frame.stack.push(module);
        frame.stack.push(aa);
    }

    //Increments bytecode counter by delta.
    private void OP_JUMP_FORWARD(PyInt count){
        frame.next_instruction +=count.value;
    }

    //If TOS is true, sets the bytecode counter to target. TOS is popped.
    public void OP_POP_JUMP_IF_TRUE(PyInt target){
        PyBool jump = (PyBool) frame.stack.pop();
        if(jump.value){
            frame.next_instruction = (int) target.value;
        }
    }

    //If TOS is false, sets the bytecode counter to target. TOS is popped.
    private void OP_POP_JUMP_IF_FALSE(PyInt target){
        PyBool top = (PyBool) frame.stack.peek();
        if(!top.value){
            frame.stack.pop();
            frame.next_instruction = (int) target.value;
        }
    }

    //If TOS is true, sets the bytecode counter to target and leaves TOS on the stack. Otherwise (TOS is false), TOS is popped.
    public void OP_JUMP_IF_TRUE_OR_POP(PyInt target){
        PyBool top = (PyBool) frame.stack.peek();
        if(top.value){
            frame.next_instruction = (int) target.value;
        }else{
            frame.stack.pop();
        }
    }

    //If TOS is false, sets the bytecode counter to target and leaves TOS on the stack. Otherwise (TOS is true), TOS is popped.
    public void OP_JUMP_IF_FALSE_OR_POP(PyInt target){
        PyBool top = (PyBool) frame.stack.peek();
        if(!top.value){
            frame.next_instruction = (int) target.value;
        }else{
            frame.stack.pop();
        }
    }

    //Set bytecode counter to target.
    public void OP_JUMP_ABSOLUTE(PyInt target){
        frame.next_instruction = (int) target.value;
    }

    //TOS is an iterator. Call its next() method. If this yields a new value, push it on the stack (leaving the iterator below it).
    // If the iterator indicates it is exhausted TOS is popped, and the bytecode counter is incremented by delta.
    public void OP_FOR_ITER(PyInt delta){
        PyIterator ite = (PyIterator) frame.stack.peek();
        PyObject obj = ite.next();
        if(obj!=null){
            frame.stack.push(obj);
        }else{
            frame.stack.pop();
            frame.next_instruction += delta.value;
        }
    }

    //Loads the global named co_names[namei] onto the stack.
    public void OP_LOAD_GLOBAL(PyObject name){

        PyObject obj;

        obj = frame.global_names.get(name);
        if(obj!=null) {
            frame.stack.push(obj);
            return;
        }

        obj = frame.builtIn.get(name);
        if(obj!=null){
            frame.stack.push(obj);
            return;
        }

        throw new RuntimeException("do not have name " + name);
    }

    //Pushes a block for a loop onto the block stack. The block spans from the current instruction with a size of delta bytes.
    public void OP_SETUP_LOOP(PyInt delta){
        Block block = new Block();
        block.toAddress = (int) delta.value + frame.next_instruction;
        block.type = Block.Type.LOOP;
        frame.blocks.push(block);

    }

    //Pushes a try block from a try-except clause onto the block stack. delta points to the first except block.
    public void OP_SETUP_EXCEPT(PyInt delta){
        Block block = new Block();
        block.toAddress = (int) delta.value + frame.next_instruction;
        block.type = Block.Type.TRY;
        frame.blocks.push(block);
    }

    //Pushes a try block from a try-except clause onto the block stack. delta points to the finally block.
    public void OP_SETUP_FINALLY(PyInt delta){
        Block block = new Block();
        block.toAddress = (int) delta.value + frame.next_instruction;
        block.type = Block.Type.TRY;
        frame.blocks.push(block);
    }

    //Store a key and value pair in a dictionary. Pops the key and value while leaving the dictionary on the stack.
    public void OP_STORE_MAP(){

        PyObject key = frame.stack.pop();
        PyObject value = frame.stack.pop();
        PyObject dict = frame.stack.pop();
        dict.__storesubscr__(key,value);
        frame.stack.push(dict);
    }

    //Pushes a reference to the local co_varnames[var_num] onto the stack.
    public void OP_LOAD_FAST(PyStr name){
        PyObject obj = frame.local_names.get(name);
        frame.stack.push(obj);
    }

    //Stores TOS into the local co_varnames[var_num].
    private void OP_STORE_FAST(PyStr name){
        PyObject obj = frame.stack.pop();
        frame.local_names.put(name,obj);
    }

    //Deletes local co_varnames[var_num].
    public void OP_DELETE_FAST(PyStr name){
        frame.local_names.remove(name);
    }

    //Pushes a reference to the cell contained in slot i of the cell and free variable storage.
    // The name of the variable is co_cellvars[i] if i is less than the length of co_cellvars. Otherwise it is co_freevars[i - len(co_cellvars)].
    public void OP_LOAD_CLOSURE(PyObject name){
        frame.stack.push(frame.cells.get(name));
    }

    //Loads the cell contained in slot i of the cell and free variable storage.
    // Pushes a reference to the object the cell contains on the stack.
    public void OP_LOAD_DEREF(PyStr name){
        frame.stack.push(frame.cells.get(name).getContent());
    }

    //Stores TOS into the cell contained in slot i of the cell and free variable storage.
    public void OP_STORE_DEREF(PyStr name){
        Cell c = new Cell();
        c.setContent(frame.stack.pop());
        frame.cells.put(name,c);
    }

    //This opcode is obsolete.
    public void OP_SET_LINENO(PyObject lineno){
    }

    //Raises an exception. argc
    // indicates the number of parameters to the raise statement,
    // ranging from 0 to 3.
    // The handler will find the traceback as TOS2,
    // the parameter as TOS1, and the exception as TOS.
    public Object OP_RAISE_VARARGS(PyInt argc){

        if(argc.value==1) {
            BaseException obj = (BaseException) frame.stack.peek();
            obj.addMsg(new PyStr(String.format("File in %s ",curFrame().code.filename.toString(),curFrame().code.name.toString())));
            frame.stack.push(obj);
            Block block = null;
            try {
                block = frame.blocks.pop();
            } catch (Exception e) {
                System.out.println("block is empty");
                return Why.EXCEPTION;
            }
            frame.next_instruction = block.toAddress;
            return null;
        }else{
            raiseVmException("not implement");
            return Why.EXCEPTION;
        }
    }

    //Calls a function. The low byte of argc indicates the number of positional parameters,
    // the high byte the number of keyword parameters.
    // On the stack, the opcode finds the keyword parameters first.
    // For each keyword argument, the value is on top of the key.
    // Below the keyword parameters, the positional parameters are on the stack,
    // with the right-most parameter on top. Below the parameters,
    // the function object to call is on the stack.
    // Pops all function arguments,
    // and the function itself off the stack,
    // and pushes the return value.
    public void OP_CALL_FUNCTION(PyInt argc){

        int low = (int) (argc.value & 0xff);
        int high = (int) ((argc.value & 0xff00)>>8);

        Map<PyStr,PyObject> kwArgs = new HashMap<>();

        for(int i=0;i<high;i++){
            PyObject value = frame.stack.pop();
            PyStr key = (PyStr) frame.stack.pop();
            kwArgs.put(key,value);
        }

        List<PyObject> args = new ArrayList<>();
        for(int i=0;i<low;i++)
            args.add(frame.stack.pop());

        Collections.reverse(args);

        Function function = (Function) frame.stack.pop();
        function.call(this,args,new PyDict(kwArgs));
        //frame.stack.push(ret);

    }

    //Pushes a new function object on the stack.
    // TOS is the code associated with the function.
    // The function object is defined to have argc default parameters,
    // which are found below TOS.
    public void OP_MAKE_FUNCTION(PyInt argc){
        Function function = new PythonFunction();
        function.code = (Code) frame.stack.pop();
        function.argc = (int) argc.value;
        frame.stack.push(function);
    }

    //Creates a new function object, sets its func_closure slot,
    // and pushes it on the stack. TOS is the code associated with the function,
    // TOS1 the tuple containing cells for the closure’s free variables.
    // The function also has argc default parameters, which are found below the cells.
    public void OP_MAKE_CLOSURE(PyInt argc){

        Function function = new PythonFunction();

        if(argc.value==0){
            function.code = (Code) frame.stack.pop();
            function.cells = (PyTuple) frame.stack.pop();
        }else{
            //not implement
            System.out.println("not implement");
        }

        frame.stack.push(function);
    }

    //Pushes a slice object on the stack. argc must be 2 or 3. If it is 2, slice(TOS1, TOS) is pushed;
    // if it is 3, slice(TOS2, TOS1, TOS) is pushed.
    // See the slice() built-in function for more information.
    public void OP_BUILD_SLICE(PyInt argc){
        if(argc.value==3){
            PyObject obj0 = frame.stack.pop();
            PyObject obj1 = frame.stack.pop();
            PyObject obj2 = frame.stack.pop();
            frame.stack.push(new PySlice(obj2,obj1,obj0));
        }else if(argc.value==2){
            PyObject obj0 = frame.stack.pop();
            PyObject obj1 = frame.stack.pop();
            frame.stack.push(new PySlice(obj1,obj0));
        }
    }

    //Prefixes any opcode which has an argument too big to fit into the default two bytes.
    // ext holds two additional bytes which, taken together with the subsequent opcode’s argument,
    // comprise a four-byte argument, ext being the two most-significant bytes.
    public void OP_EXTENDED_ARG(PyInt ext){
        frame.prefix_op_arg = (int) ext.value;
    }

    //Calls a function. argc is interpreted as in CALL_FUNCTION.
    // The top element on the stack contains the variable argument list,
    // followed by keyword and positional arguments.
    public void OP_CALL_FUNCTION_VAR(PyInt argc){

        int low = (int) (argc.value & 0xff);
        int high = (int) ((argc.value & 0xff00)>>8);

        Map<PyStr,PyObject> kwArgs = new HashMap<>();

        for(int i=0;i<high;i++){
            PyObject value = frame.stack.pop();
            PyStr key = (PyStr) frame.stack.pop();
            kwArgs.put(key,value);
        }

        List<PyObject> args = new ArrayList<>();

        PyObject varlist = frame.stack.pop();
        if(varlist instanceof PyList){
            List<PyObject> vars = ((PyList) varlist).value;
            for(int i= 0;i<vars.size(); i++ ){
                args.add(vars.get(i));
            }
        }else if(varlist instanceof PyTuple){
            PyObject [] vars = ((PyTuple) varlist).value;
            for(int i= 0;i<vars.length; i++ ){
                args.add(vars[i]);
            }
        }else {
        //
            raiseVmException("bad type");
        }

        for(int i=0;i<low;i++)
            args.add(0,frame.stack.pop());

        Function function = (Function) frame.stack.pop();
        PyObject ret = function.call(this,args,new PyDict(kwArgs));
        frame.stack.push(ret);

    }

    //Calls a function. argc is interpreted as in CALL_FUNCTION.
    // The top element on the stack contains the keyword arguments dictionary,
    // followed by explicit keyword and positional arguments.
    public void OP_CALL_FUNCTION_KW(PyInt argc){

        int low = (int) (argc.value & 0xff);
        int high = (int) ((argc.value & 0xff00)>>8);

        PyDict kw = (PyDict) frame.stack.pop();
        if( kw instanceof PyDict){
        }else{
            raiseVmException("bad argument"+kw.type());
        }

        for(int i=0;i<high;i++){
            PyObject value = frame.stack.pop();
            PyStr key = (PyStr) frame.stack.pop();
            kw.__storesubscr__(key,value);
        }


        List<PyObject> args = new ArrayList<>();
        for(int i=0;i<low;i++)
            args.add(frame.stack.pop());

        Function function = (Function) frame.stack.pop();
        Collections.reverse(args);
        PyObject ret = function.call(this,args,  kw);
        frame.stack.push(ret);
    }

    //Calls a function. argc is interpreted as in CALL_FUNCTION.
    // The top element on the stack contains the keyword arguments dictionary,
    // followed by the variable-arguments tuple, followed by explicit keyword and positional arguments.
    public void OP_CALL_FUNCTION_VAR_KW(PyInt argc){

        int low = (int) (argc.value & 0xff);
        int high = (int) ((argc.value & 0xff00)>>8);

        PyDict kw = (PyDict) frame.stack.pop();
        if( kw instanceof PyDict){
        }else{
            raiseVmException("bad argument"+kw.type());
        }

        for(int i=0;i<high;i++){
            PyObject value = frame.stack.pop();
            PyStr key = (PyStr) frame.stack.pop();
            kw.value.put(key,value);
        }

        List<PyObject> args = new ArrayList<>();

        PyObject varlist = frame.stack.pop();
        if(varlist instanceof PyList){
            List<PyObject> vars = ((PyList) varlist).value;
            for(int i=0;i<vars.size(); i++){
                args.add(vars.get(i));
            }
        }else if(varlist instanceof PyTuple){
            PyObject [] vars = ((PyTuple) varlist).value;
            for(int i= 0;i<vars.length; i++ ){
                args.add(vars[i]);
            }
        }else {
            //
            raiseVmException("bad type");
        }

        for(int i=0;i<low;i++)
            args.add(frame.stack.pop());

        Function function = (Function) frame.stack.pop();
        PyObject ret = function.call(this,args,kw);
        frame.stack.push(ret);


    }

    //This is not really an opcode.
    // It identifies the dividing line between opcodes which don’t take arguments < HAVE_ARGUMENT and those which do >= HAVE_ARGUMENT.
    public void OP_HAVE_ARGUMENT(){
    }


}
