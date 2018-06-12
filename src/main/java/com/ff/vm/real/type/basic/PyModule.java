package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

import java.util.Map;

/**
 * Created by chjun1991@163.com on 2018/6/12.
 */
public class PyModule extends PyObject {
    public PyModule(Map<PyStr, PyObject> global) {
        super();
    }

    @Override
    public String type() {
        return null;
    }

}
