package com.ff.vm.real.builtin;

import com.ff.vm.real.BuiltInFunction;
import com.ff.vm.real.PythonFunction;
import com.ff.vm.real.VirtualMachine;
import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyBool;
import com.ff.vm.real.type.basic.PyDict;
import com.ff.vm.real.type.basic.PyStr;
import com.ff.vm.real.util.CommonUtil;

import java.util.List;

/**
 * Created by chjun1991@163.com on 2018/6/8.
 */
public class BaseException extends BuiltInFunction {

    List<PyObject> msgObj;

    @Override
    public PyObject call(VirtualMachine vm, List<PyObject> args, PyDict kw) {

        return new BaseException(args);
    }

    public BaseException(List<PyObject> msgObj) {
        this.msgObj = msgObj;
    }

    @Override
    public String type() {
        return "Exception";
    }

    @Override
    public PyBool __isException__(PyObject obj0) {
        return new PyBool(true);
    }

    @Override
    public PyStr __str__() {
        return CommonUtil.toPyStr(msgObj);
    }

    public void addMsg(PyObject msg){
        msgObj.add(msg);
    }

}
