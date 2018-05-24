package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by caihaijun@navercorp.com on 2018/5/24.
 */
public class PyList extends PyObject {

    public List<PyObject> value;

    public PyList(PyObject[] arr) {
        value = Arrays.asList(arr);
    }

    @Override
    public String type() {
        return "list";
    }
}
