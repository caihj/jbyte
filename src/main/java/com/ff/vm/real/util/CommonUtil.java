package com.ff.vm.real.util;

import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyStr;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chjun1991@163.com on 2018/6/8.
 */
public class CommonUtil {

    public static PyStr  toPyStr(List<PyObject> msgObj){
         String s = "(" +  StringUtils.join(msgObj.stream().map(PyObject::toString).collect(Collectors.toList()),"\n") +")";
         return new PyStr(s);

    }
}
