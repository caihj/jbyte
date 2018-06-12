package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.constant.BasicConstant;
import com.ff.vm.tools.marshal.Constants;

/**
 * Created by chjun1991@163.com on 2018/6/1.
 */
public class PySlice extends PyObject {

    PyObject start;
    PyObject end;
    PyObject step;//None or int

    public PySlice(PyObject start, PyObject end) {
        this.start = start;
        this.end = end;
        this.step = BasicConstant.TYPE_NONE;
    }

    public PySlice(PyObject start, PyObject end, PyObject step) {
        this.start = start;
        this.end = end;
        this.step = step;
    }

    @Override
    public String type() {
        return "slice";
    }
}
