package com.ff.vm.real.type.basic;

import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.constant.BasicConstant;
import org.apache.commons.lang.StringUtils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
        value =  new LinkedHashMap();
        value.putAll(local_names);
    }

    @Override
    public PyIterator __iter__() {
        return new PyIterator(){
            Iterator<Map.Entry<PyObject, PyObject>> ite = value.entrySet().iterator();
            @Override
            public PyObject next() {
                if(ite.hasNext()){
                    return ite.next().getKey();
                }else{
                    return null;
                }
            }
        };
    }

    @Override
    public PyObject __subscr__(PyObject obj0) {
        PyObject obj = value.get(obj0);
        if(obj==null){
            throw new RuntimeException("key error");
        }else{
            return obj;
        }
    }

    @Override
    public void __delsubscr__(PyObject obj0) {
        value.remove(obj0);
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
        //return new PyStr(sb.toString().getBytes());

        return new PyStr("{"+ StringUtils.join(value.entrySet().stream().map(k->k.getKey()+": "+k.getValue()).collect(Collectors.toList()), ",")+"}");

    }

    @Override
    public PyInt __len__() {
        return new PyInt(value.size());
    }

    @Override
    public String type() {
        return "dict";
    }
}
