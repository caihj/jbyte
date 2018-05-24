package com.ff.vm.real;

import com.ff.vm.real.Code;
import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyBool;
import com.ff.vm.real.type.basic.PyInt;
import com.ff.vm.real.type.basic.PyStr;
import com.ff.vm.real.type.constant.BasicConstant;
import com.ff.vm.tools.marshal.Constants;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static com.ff.vm.real.VirtualMachineStatic.*;

/**
 * Created by caihaijun@navercorp.com on 2018/5/18.
 * https://docs.python.org/2/library/dis.html
 */
public class VirtualMachine {


    //frame stack
    private Deque<Frame> frameStack = new ArrayDeque<>();

    //current frame
    private Frame  frame = null;

    private Object return_value = null;

    private static Map<String,Method> opMethodMap = new HashMap<>();

    private static Method [] fastArray = new Method[256];


    private static Map<PyStr,PyObject> builtInConstants = new HashMap<>();

    static {
        //https://docs.python.org/2.7/library/constants.html
        builtInConstants.put(new PyStr("False"),BasicConstant.TYPE_FALSE);
        builtInConstants.put(new PyStr("True"),BasicConstant.TYPE_TRUE);
        builtInConstants.put(new PyStr("None"), BasicConstant.TYPE_NONE);
        builtInConstants.put(new PyStr("NotImplemented"), BasicConstant.TYPE_NOT_IMPLEMENT);
        builtInConstants.put(new PyStr("Ellipsis"), BasicConstant.TYPE_ELLIPSIS);
        builtInConstants.put(new PyStr("__debug__"), BasicConstant.TYPE_FALSE);
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

    public void run_code(Code code){

        Frame frame = new Frame(code, builtInConstants,Collections.EMPTY_MAP,null);
        run_frame(frame);
    }

    private Object run_frame(Frame frame){
        push_frame(frame);
        while (true){

            Triplet<String,PyObject,Integer> op = parse_byte_arg();
            Object why = dispatch(op);

            //block is not implement;


            //return;
            if(why!=null)
                break;
        }

        pop_frame();

        return return_value;

    }

    private void push_frame(Frame frame){
        frameStack.push(frame);
        this.frame = frame;
    }

    private void pop_frame(){
        frameStack.pollFirst();//equal pop ,but not throw exception.
        frame = frameStack.peekLast();
    }

    private Triplet<String,PyObject,Integer> parse_byte_arg(){

        Frame f = frame;

        int b = f.code.co_code.value[f.next_instruction++];

        PyObject argObj = null;

        if(b>=HAS_ARGUMENT){
            int arg = (short) (( 0xff & f.code.co_code.value[f.next_instruction]) + ((0xff & f.code.co_code.value[f.next_instruction+1])<<8));
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
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;

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
//        Integer obj = (Integer) frame.stack.pop();
//        Integer obj2 = ~obj;
//        frame.stack.push(obj2);
    }

    private void OP_GET_ITER(){
        Object obj = frame.stack.pop();
        throw new RuntimeException("not implement");
        //frame.stack.push();
    }

    private void OP_BINARY_POWER(){
//        Integer obj0 = (Integer) frame.stack.pop();
//        Integer obj1 = (Integer) frame.stack.pop();
//        double d = Math.pow(obj1,obj0);
//        frame.stack.push(d);
    }

    private void OP_LOAD_CONST(PyObject obj){
        frame.stack.push(obj);
    }



    private void OP_DUP_TOPX(int count){

    }

    private void OP_STORE_NAME(PyStr name){
        PyObject obj = frame.stack.pop();
        frame.local_names.put(name,obj);
    }

    private void OP_LOAD_NAME(PyStr name){
        PyObject obj = frame.local_names.get(name);
        frame.stack.push(obj);
    }

    private void OP_PRINT_ITEM(){
        PyObject obj = frame.stack.pop();
        System.out.print(new String(obj.__str__().value));
    }

    private void OP_PRINT_NEWLINE(){
        System.out.println();
    }

    private Object OP_RETURN_VALUE(){
        Object obj = frame.stack.pop();
        return_value = obj;
        return Why.RETURN;
    }

    private void OP_STORE_FAST(PyStr name){
        PyObject obj = frame.stack.pop();
        frame.local_names.put(name,obj);
    }

    private void OP_LOAD_FAST(PyStr name){
        PyObject obj = frame.local_names.get(name);
        frame.stack.push(obj);
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

    private void OP_POP_JUMP_IF_FALSE(PyInt count){
        PyBool top = (PyBool) frame.stack.pop();
        if(!top.value){
            frame.next_instruction = (int) count.value;
        }
    }

    private void OP_JUMP_FORWARD(PyInt count){
        frame.next_instruction +=count.value;
    }


}
