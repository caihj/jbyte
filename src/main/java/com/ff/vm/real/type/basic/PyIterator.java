package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;
import com.ff.vm.tools.marshal.Constants;

/**
 * Created by chjun1991@163.com on 2018/5/25.
 */
public abstract class PyIterator extends PyObject {

    @Override
    public  String type() {
        return "iterator";
    }

    public abstract PyObject next();
}
