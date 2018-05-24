package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

/**
 * Created by caihaijun@navercorp.com on 2018/5/24.
 */
public class PyFloat extends PyObject{

    public double value;

    public PyFloat(double d) {
        this.value = d;
    }

    @Override
    public String type() {
        return "float";
    }

    @Override
    public PyStr __str__() {
        return new PyStr(value+"");
    }
}
