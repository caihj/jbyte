package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

/**
 * Created by chjun1991@163.com on 2018/6/7.
 */
public class PyClassInstance extends PyObject {

    private PyClass pyClass;

    private PyDict atttr ;

    public PyClassInstance(PyClass pyClass) {
        this.pyClass = pyClass;
        this.atttr = new PyDict();
    }

    @Override
    public String type() {
        return pyClass.type();
    }

    public void __store_attr__(PyStr name, PyObject obj1) {
        atttr.__storesubscr__(name,obj1);
    }

    public PyObject __attr__(PyStr name) {
        return atttr.__subscr__(name);
    }

}
