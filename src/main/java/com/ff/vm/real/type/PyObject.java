package com.ff.vm.real.type;

import com.ff.vm.real.type.basic.PyStr;

/**
 * Created by caihaijun@navercorp.com on 2018/5/24.
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
}
