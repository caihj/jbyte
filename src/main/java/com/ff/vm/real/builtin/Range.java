package com.ff.vm.real.builtin;

import com.ff.vm.real.BuiltInFunction;
import com.ff.vm.real.Function;
import com.ff.vm.real.VirtualMachine;
import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyDict;
import com.ff.vm.real.type.basic.PyInt;
import com.ff.vm.real.type.basic.PyList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by chjun1991@163.com on 2018/5/28.
 */
public class Range extends BuiltInFunction {
    @Override
    public PyObject call( List<PyObject> args, PyDict kw) {
        if(args.size()==1){
            return getList(0L,((PyInt)args.get(0)).value,1);
        }else if(args.size()==2 ){
            return getList(((PyInt)args.get(0)).value,((PyInt)args.get(1)).value,1);
        }else if(args.size()==3){
            return getList(((PyInt)args.get(0)).value,((PyInt)args.get(1)).value,((PyInt)args.get(2)).value);
        }else{
            throw new UnsupportedOperationException("");
        }
    }

    public PyList getList(long start,long stop,long step){
        List<PyObject> temp = new ArrayList<>();
        for(long i=start;i<stop;i+=step){
            temp.add(new PyInt(i));
        }
        return new PyList(temp);
    }
}
