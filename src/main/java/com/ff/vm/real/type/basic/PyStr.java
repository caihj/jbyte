package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;

import java.io.UnsupportedEncodingException;
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

    @Override
    public String toString() {
        return "'" +(new String(value))+"'";
    }

    @Override
    public PyObject __mod__(PyObject obj) {
        PyTuple tuple = (PyTuple) obj;
        Object []  arr = new Object[tuple.value.length];
        for(int i=0;i<arr.length;i++){
            arr[i] = tuple.value[i].toJavaObject();
        }

        try {
            return new PyStr(String.format(new String(this.value,"utf-8"),arr).getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new PyStr("format error");
        }
    }

    @Override
    public Object toJavaObject() {
        return new String(value);
    }
}
