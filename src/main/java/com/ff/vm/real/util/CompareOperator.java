package com.ff.vm.real.util;

import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyBool;
import com.ff.vm.real.type.basic.PyInt;

/**
 * Created by chjun1991@163.com on 2018/5/25.
 */
public class CompareOperator {

    public static PyBool  lt(PyObject op1,PyObject op2){
        return  op1.__lt__(op2);
    }
    public static PyBool  le(PyObject op1,PyObject op2){
        return  op1.__le__(op2);
    }
    public static PyBool  eq(PyObject op1,PyObject op2){
        return  op1.__eq__(op2);
    }
    public static PyBool  ne(PyObject op1,PyObject op2){
        return  op1.__ne__(op2);
    }
    public static PyBool  gt(PyObject op1,PyObject op2){
        return  op1.__gt__(op2);
    }
    public static PyBool  ge(PyObject op1,PyObject op2){
        return  op1.__ge__(op2);
    }
    public static PyBool  in(PyObject op1,PyObject op2){
        return  op1.__in__(op2);
    }
    public static PyBool  not_in(PyObject op1,PyObject op2){
        return  op1.__not_in__(op2);
    }
    public static PyBool  is(PyObject op1,PyObject op2){
        return  op1.__is__(op2);
    }
    public static PyBool  is_not(PyObject op1,PyObject op2){
        return  op1.__is_not__(op2);
    }
    public static PyBool  isException(PyObject op1,PyObject op2){
        return  op1.__isException__(op2);
    }

    public static PyBool op(PyInt op, PyObject op1, PyObject op2){
        switch ((int) op.value){
            case 0:return lt(op1,op2);
            case 1:return le(op1,op2);
            case 2:return eq(op1,op2);
            case 3:return ne(op1,op2);
            case 4:return gt(op1,op2);
            case 5:return ge(op1,op2);
            case 6:return in(op1,op2);
            case 7:return not_in(op1,op2);
            case 8:return is(op1,op2);
            case 9:return is_not(op1,op2);
            case 10:return isException(op1,op2);
        }
        throw new RuntimeException("not supported operation "+op.value);
    }

}
