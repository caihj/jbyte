package com.ff.vm.real.builtin;

import com.ff.vm.real.BuiltInFunction;
import com.ff.vm.real.VirtualMachine;
import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyBool;
import com.ff.vm.real.type.basic.PyDict;
import com.ff.vm.real.type.basic.PyStr;
import com.ff.vm.real.util.CommonUtil;

import java.util.List;

public class VmException extends BaseException {


    public VmException(List<PyObject> msgObj) {
        super(msgObj);
    }

    @Override
    public String type() {
        return "VmException";
    }
}
