package com.ff.vm.real;

import com.ff.vm.real.Code;
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

        Frame frame = new Frame(code, Collections.EMPTY_MAP,Collections.EMPTY_MAP,null);
        run_frame(frame);
    }

    private Object run_frame(Frame frame){
        push_frame(frame);
        while (true){

            Triplet<String,Object,Integer> op = parse_byte_arg();
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

    private Triplet<String,Object,Integer> parse_byte_arg(){

        Frame f = frame;

        int b = f.code.co_code[f.next_instruction++];

        Object argObj = null;

        if(b>=HAS_ARGUMENT){
            short arg = (short) (( 0xff & f.code.co_code[f.next_instruction]) + ((0xff & f.code.co_code[f.next_instruction+1])<<8));
            if(hasconst.contains((int)b)){
                argObj = f.code.co_consts[arg];
            }else if(hasfree.contains(b)){

                if(arg<f.code.co_cellvars.length){
                    argObj = f.code.co_cellvars[arg];
                }else{
                    int var_idx = arg - f.code.co_cellvars.length;
                    argObj = f.code.co_freevars[var_idx];
                }
            }else if(hasname.contains(b)){
                argObj = f.code.co_names[arg];
            }else if(hasjrel.contains(b)){
                argObj = f.next_instruction + arg;
            }else if(hasjabs.contains(b)){
                argObj = arg;
            }else if(haslocal.contains(b)){
                argObj = f.code.co_varnames[arg];
            }else{
                argObj = arg;
            }

            f.next_instruction+=2;
        }

        Triplet<String,Object,Integer> op = new Triplet(opcodeTostr(b),argObj,b);

        return op;
    }


    private Object dispatch(Triplet<String,Object,Integer> op){

        String opCode = op.getValue0();
        Object arg = op.getValue1();


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


    private void OP_LOAD_CONST(Object obj){
        frame.stack.push(obj);
    }

    private void OP_POP_TOP(){
        frame.stack.pop();
    }

    private void OP_DUP_TOP(){
        frame.stack.push(frame.stack.peek());
    }

    private void OP_DUP_TOPX(int count){

    }

    private void OP_STORE_NAME(String name){
        Object obj = frame.stack.pop();
        frame.local_names.put(name,obj);
    }

    private void OP_LOAD_NAME(String name){
        Object obj = frame.local_names.get(name);
        frame.stack.push(obj);
    }

    private void OP_PRINT_ITEM(){
        Object obj = frame.stack.pop();
        System.out.print(obj);
    }

    private void OP_PRINT_NEWLINE(){
        System.out.println();
    }

    private Object OP_RETURN_VALUE(){
        Object obj = frame.stack.pop();
        return_value = obj;
        return Why.RETURN;
    }

    private void OP_STORE_FAST(String name){
        Object obj = frame.stack.pop();
        frame.local_names.put(name,obj);
    }

    private void OP_LOAD_FAST(String name){
        Object obj = frame.local_names.get(name);
        frame.stack.push(obj);
    }


    private void OP_BINARY_ADD(){
        Object obj0 = frame.stack.pop();
        Object obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        Object obj3 = Long.valueOf(String.valueOf(obj1)) + Long.valueOf(String.valueOf(obj0));
        frame.stack.push(obj3);
    }

    private void OP_BINARY_SUBTRACT(){
        Object obj0 = frame.stack.pop();
        Object obj1 = frame.stack.pop();
        //the implement is wrong ,just support int
        Object obj3 = Long.valueOf(String.valueOf(obj1)) - Long.valueOf(String.valueOf(obj0));
        frame.stack.push(obj3);
    }

    private void OP_POP_JUMP_IF_FALSE(Integer count){
        boolean top = (boolean) frame.stack.pop();
        if(!top){
            frame.next_instruction = count;
        }
    }


}
