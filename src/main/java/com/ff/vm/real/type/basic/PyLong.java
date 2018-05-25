package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

import java.math.BigDecimal;

/**
 * Created by chjun1991@163.com on 2018/5/24.
 */
public class PyLong extends PyObject {

    public BigDecimal value;

    public PyLong(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String type() {
        return "long";
    }

    @Override
    public PyStr __str__() {
        return new PyStr(value+"");
    }
}
