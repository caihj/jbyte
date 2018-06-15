package com.ff.vm.real;

import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyDict;
import com.ff.vm.real.type.basic.PyTuple;
import com.ff.vm.real.type.constant.BasicConstant;
import com.ff.vm.tools.marshal.Constants;

import java.util.List;

/**
 * Created by chjun1991@163.com on 2018/5/18.
 */
public abstract class Function extends PyObject{

    public Code code;

    public int argc;

    ///for clourse
    public PyTuple cells = new PyTuple();

    @Override
    public String type() {
        return "function";
    }

    public  void call(VirtualMachine vm, List<PyObject> args, PyDict kw){
        PyObject ret = call(args,kw);
        vm.curFrame().stack.push(ret);
    }

    protected  PyObject call( List<PyObject> args, PyDict kw){
        return BasicConstant.TYPE_NONE;
    };


}
