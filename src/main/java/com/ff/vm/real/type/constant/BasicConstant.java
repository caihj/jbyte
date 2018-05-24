package com.ff.vm.real.type.constant;

import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyBool;
import com.ff.vm.real.type.basic.PyStr;

/**
 * Created by caihaijun@navercorp.com on 2018/5/24.
 */
public class BasicConstant {

    public static PyObject TYPE_STOPITER = new PyObject() {
        @Override
        public String type() {
            return "TYPE_STOPITER";
        }
    };

    public static PyObject TYPE_NOT_IMPLEMENT = new PyObject() {
        @Override
        public String type() {
            return "TYPE_NOT_IMPLEMENT";
        }
    };

    public static PyObject TYPE_ELLIPSIS = new PyObject() {
        @Override
        public String type() {
            return "TYPE_ELLIPSIS";
        }
    };


    public static PyObject TYPE_NULL = new PyObject() {
        @Override
        public String type() {
            return "TypeNull";
        }
    };

    public static PyObject TYPE_NONE = new PyObject() {
        @Override
        public String type() {
            return "TypeNone";
        }

        @Override
        public PyStr __str__() {
            return new PyStr("None");
        }
    };

    public static PyBool TYPE_TRUE = new  PyBool(true);

    public static PyBool TYPE_FALSE = new PyBool(false);
}
