package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

import java.util.LinkedHashMap;

/**
 * Created by caihaijun@navercorp.com on 2018/5/24.
 */
public class PyDict extends PyObject {

    public LinkedHashMap<PyObject,PyObject> value;

    @Override
    public String type() {
        return "dict";
    }
}
