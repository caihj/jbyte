package com.ff.vm.real.builtin;

import com.ff.vm.real.BuiltInFunction;
import com.ff.vm.real.VirtualMachine;
import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyBool;
import com.ff.vm.real.type.basic.PyStr;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by caihaijun@navercorp.com on 2018/5/31.
 */
public class UserWarning extends BuiltInFunction {
    @Override
    public PyObject call(VirtualMachine vm, List<PyObject> args) {
        return new PyObject() {

            List<PyObject> msgObj = args;


            @Override
            public String type() {
                return "UserWarning";
            }

            @Override
            public PyBool __isException__(PyObject obj0) {
                return new PyBool(true);
            }

            @Override
            public PyStr __str__() {
                StringBuilder sb = new StringBuilder();
                sb.append("(");
                for(int i=msgObj.size()-1;i>=0;i--){
                    sb.append(msgObj.get(i).__str__().toString());
                    sb.append(", ");
                }
                sb.deleteCharAt(sb.length()-1);
                sb.append(")");
                return new PyStr(sb.toString());
            }
        };
    }
}
