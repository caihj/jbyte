package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chjun1991@163.com on 2018/5/24.
 */
public class PyList extends PyObject {

    public List<PyObject> value;

    public PyList(List<PyObject> arr) {
        value = arr;
    }

    @Override
    public String type() {
        return "list";
    }

    @Override
    public PyIterator __iter__() {
        return new PyIterator(){
            Iterator<PyObject> ite = value.iterator();
            @Override
            public PyObject next() {
                if(ite.hasNext()){
                    return ite.next();
                }else{
                    return null;
                }
            }
        };
    }


    @Override
    public void __delsubscr__(PyObject indx){
        PyInt pyInt = (PyInt) indx;
        value.remove(pyInt.value);
    }

    public void append(PyObject val) {
    }

    @Override
    public PyStr __str__() {
        return new PyStr("("+ StringUtils.join(value.stream().map(PyObject::toString).collect(Collectors.toList()), ", ")+")");
    }

    @Override
    public PyInt __len__() {
        return new PyInt(value.size());
    }
}
