package com.ff.vm.real.builtin;

import com.ff.vm.real.BuiltInFunction;
import com.ff.vm.real.VirtualMachine;
import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyBool;
import com.ff.vm.real.type.basic.PyDict;
import com.ff.vm.real.type.basic.PyStr;

import java.util.List;

public class VmException extends BuiltInFunction {
    @Override
    public PyObject call(VirtualMachine vm, List<PyObject> args, PyDict kw) {
        return new PyObject() {

            List<PyObject> msgObj = args;

            @Override
            public String type() {
                return "VmException";
            }

            @Override
            public PyBool __isException__(PyObject obj0) {
                return new PyBool(true);
            }

            @Override
            public PyStr __str__() {
                StringBuilder sb = new StringBuilder();
                sb.append("(");
                for (int i = msgObj.size() - 1; i >= 0; i--) {
                    sb.append(msgObj.get(i).toString());
                    sb.append(", ");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(")");
                return new PyStr(sb.toString());
            }
        };
    }
}
