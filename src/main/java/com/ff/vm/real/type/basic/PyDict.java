package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by chjun1991@163.com on 2018/5/24.
 */
public class PyDict extends PyObject {

    public LinkedHashMap<PyObject,PyObject> value;

    public PyDict(PyInt capacity){
        value = new LinkedHashMap((int)capacity.value);
    }

    public PyDict(){
        value = new LinkedHashMap<>();
    }

    public PyDict(Map<PyStr, PyObject> local_names) {
        value.putAll(local_names);
    }

    @Override
    public String type() {
        return "dict";
    }
}
