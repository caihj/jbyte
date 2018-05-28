package com.ff.vm.real;

import com.ff.vm.real.type.PyObject;

import java.util.List;

/**
 * Created by chjun1991@163.com on 2018/5/18.
 */
public abstract class Function extends PyObject{

    public Code code;

    public int argc;

    @Override
    public String type() {
        return "function";
    }

    public abstract PyObject call(VirtualMachine vm, List<PyObject> args);
}
