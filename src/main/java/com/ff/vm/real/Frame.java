package com.ff.vm.real;

import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyStr;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by chjun1991@163.com on 2018/5/18.
 * https://docs.python.org/2.0/ref/execframes.html
 */
public class Frame {

     Code code;

     Map<PyStr,PyObject> global_names = new HashMap<>();

     Map<PyStr,PyObject> local_names = new HashMap<>();

     Frame prev_frame;

     Stack<PyObject> stack = new Stack();

     Stack<Block> blocks = new Stack<>();

     int next_instruction = 0;

     PyObject lastException = null;

     PyObject returnValue = null;

     Map<PyStr,Cell> cells = new HashMap<>();

     int prefix_op_arg = 0;

    public Frame(Code code,Map<PyStr,PyObject> global_names, Map<PyStr,PyObject> local_names,Frame prev_frame,Map<PyStr,Cell> cells){
        this.code = code;
        this.global_names.putAll(global_names);
        this.local_names.putAll(local_names);
        this.local_names.putAll(global_names);
        this.prev_frame = prev_frame;
        if(cells!=null)
            this.cells.putAll(cells);
    }

    public void print(){
        System.out.println("next_instruction:"+next_instruction);
    }


}
