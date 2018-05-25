package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

import java.util.LinkedHashMap;

/**
 * Created by caihaijun@navercorp.com on 2018/5/24.
 */
public class PyDict extends PyObject {

    public LinkedHashMap<PyObject,PyObject> value;

    public PyDict(PyInt capacity){
        value = new LinkedHashMap((int)capacity.value);
    }

    public PyDict(){
        value = new LinkedHashMap<>();
    }

    @Override
    public String type() {
        return "dict";
    }
}
