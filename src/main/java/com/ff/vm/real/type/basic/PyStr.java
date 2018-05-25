package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

import java.util.Arrays;

/**
 * Created by chjun1991@163.com on 2018/5/24.
 */

public class PyStr extends PyObject {

    public byte [] value;

    public PyStr(String str){
        this.value = str.getBytes();
    }

    public PyStr(byte[] arr) {
        this.value = arr;
    }

    @Override
    public String type() {
        return "str";
    }

    @Override
    public PyStr __str__() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PyStr pyStr = (PyStr) o;
        return Arrays.equals(value, pyStr.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }
}
