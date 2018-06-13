package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by chjun1991@163.com on 2018/5/24.
 */
public class PyTuple extends PyObject {

    public PyObject [] value;

    public PyTuple(){
        value = new PyObject[0];
    }

    public PyTuple(PyObject[] tuple) {
        this.value = tuple;
    }

    @Override
    public String type() {
        return "tuple";
    }

    @Override
    public PyIterator __iter__() {
        return new PyIterator(){
           int idx=0;
            @Override
            public PyObject next() {
                if(idx<value.length){
                    return value[idx++];
                }else{
                    return null;
                }
            }
        };
    }
}
