package com.ff.vm.real.type;

import com.ff.vm.real.type.basic.PyStr;

/**
 * Created by caihaijun@navercorp.com on 2018/5/24.
 * root class for a python object
 */
public abstract class PyObject {

    public abstract String type();

    public PyStr convertToStr(){ return new PyStr("not implement".getBytes());}

    public PyObject __add__(PyObject obj){ throw new RuntimeException("not supported type "+ this.type() + " "+obj.type());}

    public PyObject __sub__(PyObject obj){ throw new RuntimeException("not supported type "+ this.type() + " "+obj.type());}

    public PyStr __str__(){return new PyStr(super.toString());}

    @Override
    public String toString() {
        return new String(__str__().value);
    }
}
