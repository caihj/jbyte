package com.ff.vm.real.builtin;

import com.ff.vm.real.BuiltInFunction;
import com.ff.vm.real.VirtualMachine;
import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyDict;

import java.util.List;

public class Len  extends BuiltInFunction {
    @Override
    public PyObject call(VirtualMachine vm, List<PyObject> args, PyDict kw) {
        return args.get(0).__len__();
    }
}
