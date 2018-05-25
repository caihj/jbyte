package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by caihaijun@navercorp.com on 2018/5/25.
 * https://docs.python.org/2/tutorial/datastructures.html#sets
 */
public class PySet extends PyObject {

    public Set<PyObject> value = new HashSet<>();

    public PySet(PyObject[] arr) {
        value.addAll(Arrays.asList(arr));
    }


    @Override
    public String type() {
        return "set";
    }
}
