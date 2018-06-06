package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

/**
 * Created by caihaijun@navercorp.com on 2018/6/6.
 */
public class PyClass extends PyObject {

    private PyStr name;

    private PyTuple base;

    private PyDict atttr;

    public PyClass(PyStr name, PyTuple base, PyDict atttr) {
        this.name = name;
        this.base = base;
        this.atttr = atttr;
    }

    @Override
    public String type() {
        return name.toString();
    }
}
