package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

import java.math.BigDecimal;

/**
 * Created by caihaijun@navercorp.com on 2018/5/24.
 */
public class PyInt extends PyObject {

    public long value;

    public PyInt(long value) {
        this.value = value;
    }

    @Override
    public String type() {
        return "int";
    }

    @Override
    public PyObject __add__(PyObject obj) {
        switch (obj.type()){
            case "int":return new PyInt(this.value + ((PyInt) obj).value);
            case "float":return new PyFloat(this.value + ((PyFloat)obj).value);
            case "long":return new PyLong(new BigDecimal(this.value).add(((PyLong)obj).value));
        }

        return super.__add__(obj);
    }

    @Override
    public PyObject __sub__(PyObject obj) {
        switch (obj.type()){
            case "int":return new PyInt(this.value - ((PyInt) obj).value);
            case "float":return new PyFloat(this.value - ((PyFloat)obj).value);
            case "long":return new PyLong(new BigDecimal(this.value).subtract(((PyLong)obj).value));
        }

        return super.__sub__(obj);
    }

    @Override
    public PyStr __str__() {
        return new PyStr(value+"");
    }
}
