package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;
import com.ff.vm.tools.marshal.Constants;

/**
 * Created by chjun1991@163.com on 2018/5/25.
 */
public class PyIterator extends PyObject {

    PyObject obj;

    public PyIterator(PyObject obj){
        //check method
        this.obj = obj;
    }

    @Override
    public String type() {
        return null;
    }

    public PyObject next(){
        return null;
    }
}
