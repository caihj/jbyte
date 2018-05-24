package com.ff.vm.real;

import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyInt;
import com.ff.vm.real.type.basic.PyList;
import com.ff.vm.real.type.basic.PyStr;
import com.ff.vm.real.type.basic.PyTuple;

/**
 * Created by caihaijun@navercorp.com on 2018/5/18.
 */
public class Code extends PyObject{

    public PyInt argcount;

    public PyInt nlocals;

    public PyInt stacksize;

    public PyInt flags;


    //because of in java, byte means signed byte
    //so we use short to represent  byte from 0x00~0xff, and it's all positive number.
    public PyStr co_code ;

    //const
    public PyTuple co_consts;

    //names
    public PyTuple co_names;

    public PyTuple co_varnames;

    public PyTuple co_cellvars;

    public PyTuple co_freevars;

    public PyStr filename;

    public PyStr name;

    public PyInt firstlineno;

    @Override
    public String type() {
        return "Object Code";
    }

    //lnotab;

}
