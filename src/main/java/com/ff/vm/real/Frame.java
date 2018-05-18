package com.ff.vm.real;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by caihaijun@navercorp.com on 2018/5/18.
 */
public class Frame {

     Code code;

     Map<String,Object> global_names = new HashMap<>();

     Map<String,Object> local_names = new HashMap<>();

     Frame prev_frame;

     Stack<Object> stack = new Stack();

     int next_instruction = 0;

    public Frame(Code code,Map<String,Object> global_names, Map<String,Object> local_names,Frame prev_frame){
        this.code = code;
        this.global_names.putAll(global_names);
        this.local_names.putAll(local_names);
        this.prev_frame = prev_frame;
    }


}
