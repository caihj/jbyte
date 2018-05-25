package com.ff.vm.real.type;

import com.ff.vm.real.type.basic.PyStr;

/**
 * Created by chjun1991@163.com on 2018/5/24.
 * root class for a python object
 */
public abstract class PyObject {

    public abstract String type();

    public PyStr convertToStr(){ return new PyStr("not implement".getBytes());}

    public PyObject __add__(PyObject obj){ throw new RuntimeException("not supported type "+ this.type() + " "+obj.type());}

    public PyObject __sub__(PyObject obj){ throw new RuntimeException("not supported type "+ this.type() + " "+obj.type());}

    public  PyObject __mul__(PyObject obj){ throw new RuntimeException("not supported type "+ this.type() + " "+obj.type());}

    public PyObject __div__(PyObject obj) { throw new RuntimeException("not supported type "+ this.type() + " "+obj.type());}

    public PyObject __floordiv__(PyObject obj) { throw new RuntimeException("not supported type "+ this.type() + " "+obj.type());}

    public PyObject  __truediv__(PyObject obj) { throw new RuntimeException("not supported type "+ this.type() + " "+obj.type());}

    public PyObject  __mod__(PyObject obj) { throw new RuntimeException("not supported type "+ this.type() + " "+obj.type());}

    public PyObject  __divmod__(PyObject obj) { throw new RuntimeException("not supported type "+ this.type() + " "+obj.type());}

    public PyObject __SUBSCR__(PyObject obj) { throw new RuntimeException("not supported type "+ this.type() + " "+obj.type());}

    public PyStr __str__(){return new PyStr(super.toString());}

    @Override
    public String toString() {
        return new String(__str__().value);
    }


    public PyObject __subscr__(PyObject obj0) { throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());}

    public PyObject __lshift__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());  }

    public PyObject __rshift__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type()); }

    public PyObject __and__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __xor__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __or__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __pow__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __slice0__() {throw new RuntimeException("not supported type "+ this.type());
    }

    public PyObject __slice1__(PyObject begin) {throw new RuntimeException("not supported type "+ this.type() + " "+begin.type());
    }


    public PyObject __slice2__(PyObject end) {throw new RuntimeException("not supported type "+ this.type() + " "+end.type());
    }

    public PyObject __slice3__(PyObject begin,PyObject end) {
        throw new RuntimeException("not supported type "+ this.type() + " ["+begin.type() +":"+ end.type());
    }

    public PyObject __ior__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __ixor__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __iand__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __irshift__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __ilshift__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __isub__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __iadd__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __imod__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __itruediv__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __ifloordiv__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __idiv__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __imul__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __ipow__(PyObject obj0) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __store_slice1__(PyObject obj0, PyObject obj2) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __store_slice3__(PyObject obj1, PyObject obj0, PyObject obj3) {
        throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public PyObject __store_slice2__(PyObject obj0, PyObject obj2) {throw new RuntimeException("not supported type "+ this.type() + " "+obj0.type());
    }

    public void __delslice__() {
    }

    public void __delslice1__(PyObject obj0) {
    }

    public void __delslice2__(PyObject obj0) {
    }

    public void __delslice3__(PyObject obj1, PyObject obj0) {
    }

    public void __storesubscr__(PyObject obj0, PyObject obj2) {
    }

    public void __delsubscr__(PyObject obj0) {
    }

    public void __print__(PyObject item){

    }

   
    public void __store_attr__(PyStr name, PyObject obj1) {
    }

    public void __del_attr__(PyStr name, PyObject obj1) {
    }

    public PyObject __attr__(PyStr name) {
        return null;
    }
}
