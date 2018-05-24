package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

public class PyComplex extends PyObject {
    public double real;
    public double image;

    @Override
    public String type() {
        return "complex";
    }
}
