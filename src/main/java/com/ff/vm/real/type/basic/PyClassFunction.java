package com.ff.vm.real.type.basic;

import com.ff.vm.real.PythonFunction;
import com.ff.vm.real.VirtualMachine;
import com.ff.vm.real.type.PyObject;

import java.util.List;

/**
 * Created by caihaijun@navercorp.com on 2018/6/7.
 */
public class PyClassFunction extends PythonFunction {

    public PyClassInstance self;

    @Override
    public PyObject call(VirtualMachine vm, List<PyObject> args, PyDict kw) {
        args.add(self);
        return super.call(vm, args, kw);
    }
}
