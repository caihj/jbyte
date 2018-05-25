package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

import java.util.List;

/**
 * Created by chjun1991@163.com on 2018/5/24.
 */
public class PyTuple extends PyObject {

    public PyObject [] value;

    public PyTuple(PyObject[] tuple) {
        this.value = tuple;
    }

    @Override
    public String type() {
        return "tuple";
    }
}
