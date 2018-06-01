package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.constant.BasicConstant;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by chjun1991@163.com on 2018/5/24.
 */
public class PyDict extends PyObject {

    public LinkedHashMap<PyObject,PyObject> value;

    public PyDict(PyInt capacity){
        value = new LinkedHashMap((int)capacity.value);
    }

    public PyDict(){
        value = new LinkedHashMap<>();
    }

    public PyDict(Map<PyStr, PyObject> local_names) {
        value.putAll(local_names);
    }

    @Override
    public PyObject __subscr__(PyObject obj0) {
        PyObject obj = value.get(obj0);
        if(obj==null){
            return BasicConstant.TYPE_NONE;
        }else{
            return obj;
        }
    }

    @Override
    public void __storesubscr__(PyObject obj0, PyObject obj2) {
        value.put(obj0,obj2);
    }

    @Override
    public PyStr __str__() {

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for(Map.Entry<PyObject,PyObject> kv:value.entrySet()){
            sb.append(kv.getKey().toString());
            sb.append(":");
            sb.append(kv.getValue().toString());
            sb.append(",");
        }

        sb.replace(sb.length()-1,sb.length(),"}");
        return new PyStr(sb.toString().getBytes());

    }

    @Override
    public String type() {
        return "dict";
    }
}
