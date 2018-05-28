package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chjun1991@163.com on 2018/5/24.
 */
public class PyList extends PyObject {

    public List<PyObject> value;

    public PyList(List<PyObject> arr) {
        value = arr;
    }

    @Override
    public String type() {
        return "list";
    }

    @Override
    public PyIterator __iter__() {
        return new PyIterator(){
            Iterator<PyObject> ite = value.iterator();
            @Override
            public PyObject next() {
                if(ite.hasNext()){
                    return ite.next();
                }else{
                    return null;
                }
            }
        };
    }

    public void append(PyObject val) {
    }
}
