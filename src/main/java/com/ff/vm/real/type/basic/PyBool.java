package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

/**
 * Created by caihaijun@navercorp.com on 2018/5/24.
 */
public class PyBool extends PyObject {

    public boolean value;

    public PyBool(boolean b) {
        this.value = b;
    }

    @Override
    public String type() {
        return "bool";
    }
}
