package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

import java.util.Map;

/**
 * Created by chjun1991@163.com on 2018/6/12.
 */
public class PyModule extends PyObject {

    public Map<PyStr,PyObject> attr;

    public PyModule(Map<PyStr, PyObject> attr) {
        this.attr = attr;
    }

    @Override
    public String type() {
        return "module";
    }


    @Override
    public PyObject __attr__(PyStr name) {
        PyObject at =  attr.get(name);
        if(at==null){
            throw new RuntimeException("bad attribute");
        }else{
            return at;
        }
    }



}
