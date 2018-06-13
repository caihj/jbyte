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

    public Code code;

    public Map<PyStr,PyObject> global_names = new HashMap<>();

    public Map<PyStr,PyObject> local_names = new HashMap<>();

    public Map<PyStr,PyObject> builtIn;

    public Frame prev_frame;

    public Stack<PyObject> stack = new Stack();

    public Stack<Block> blocks = new Stack<>();

    public int next_instruction = 0;

    public PyObject lastException = null;

    public PyObject returnValue = null;

    public Map<PyStr,Cell> cells = new HashMap<>();

    public int prefix_op_arg = 0;

    public Frame(Code code,Map<PyStr,PyObject> global_names, Map<PyStr,PyObject> local_names,Map<PyStr,PyObject> builtIn,Frame prev_frame,Map<PyStr,Cell> cells){
        this.code = code;
        this.global_names.putAll(global_names);
        this.local_names.putAll(local_names);
        this.builtIn = builtIn;
        this.prev_frame = prev_frame;
        if(cells!=null)
            this.cells.putAll(cells);
    }

    public void print(){
        System.out.println(String.format(" name %s next_instruction:%d",code.name,next_instruction));
    }


}
