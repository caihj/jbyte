package com.ff.vm.toy;

import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by caihaijun@navercorp.com on 2018/5/18.
 */
public class VirtualMachine {

    private Stack<Object> stack = new Stack();

    private Map<String,Object> environment = new HashMap<>();

    public void run_code(Code code){
        for(Pair<String,Integer> op:code.instructions){

            String opt = op.getValue0();
            Object optNumber = null;

            String name = null;

            if(opt.contains("NAME")){
                name = code.names.get(op.getValue1());
            }else{
                optNumber = op.getValue1() == null ? null : code.numbers.get(op.getValue1());
            }

              switch (opt){
                  case "LOAD_VALUE":LOAD_VALUE(optNumber);break;
                  case "PRINT_ANSWER":PRINT_ANSWER();break;
                  case "ADD_TWO_VALUES":ADD_TWO_VALUES();break;
                  case "STORE_NAME":STORE_NAME(name); break;
                  case "LOAD_NAME":LOAD_NAME(name); break;
                  default:
                      throw new RuntimeException("bad instruct");
              }
        }
    }

    private void LOAD_VALUE(Object number){
        stack.push(number);
    }


    private void PRINT_ANSWER(){
        Object o = stack.pop();
        System.out.println(o);
    }

    private void ADD_TWO_VALUES(){

        Object o1 = stack.pop();
        Object o2 = stack.pop();
        Object o3 = (Integer)o1 + (Integer) o2;

        stack.push(o3);
    }

    private void STORE_NAME(String name){
        Object val = stack.pop();
        environment.put(name,val);
    }

    private void LOAD_NAME(String name){
        Object val = environment.get(name);
        stack.push(val);
    }

}
