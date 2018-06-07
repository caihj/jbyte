package com.ff.vm.real.type.basic;

import com.ff.vm.real.PythonFunction;
import com.ff.vm.real.VirtualMachine;
import com.ff.vm.real.type.PyObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caihaijun@navercorp.com on 2018/6/6.
 * class init function object
 * aka construct object.
 */
public class PyClass extends PythonFunction {

    private PyStr name;

    private PyTuple base;

    private PyDict atttr;

    public PyClass(PyStr name, PyTuple base, PyDict atttr) {
        this.name = name;
        this.base = base;
        this.atttr = atttr;
        for(Map.Entry<PyObject,PyObject> kv:atttr.value.entrySet()){
            PyObject v = kv.getValue();
            if(v instanceof PythonFunction){
                PythonFunction function = (PythonFunction) v;
                kv.setValue(new PythonFunction(){
                    @Override
                    public PyObject call(VirtualMachine vm, List<PyObject> args, PyDict kw) {
                        kw.__storesubscr__(new PyStr("self"),this);
                        return function.call(vm,args,kw);
                    }
                });
            }

        }
    }

    @Override
    public String type() {
        return name.toString();
    }

    @Override
    public PyObject call(VirtualMachine vm, List<PyObject> args, PyDict kw) {
        PythonFunction init = (PythonFunction) atttr.value.get(new PyStr("__init__"));
        PyClassInstance classIntance = new PyClassInstance();
        args.add(0,classIntance);
        //kw.__storesubscr__(init.code.co_varnames.value[0],classIntance);
        init.call(vm,args,kw);

        for(Map.Entry<PyObject,PyObject> kv:atttr.value.entrySet()){
            PyObject attr = kv.getValue();
            if(attr instanceof  PythonFunction){
                PythonFunction unbindFunction = (PythonFunction) attr;
                PyClassFunction bindFunction = new PyClassFunction();
                bindFunction.code = unbindFunction.code;
                bindFunction.argc = unbindFunction.argc;
                bindFunction.cells = unbindFunction.cells;
                bindFunction.self = classIntance;

                classIntance.__store_attr__((PyStr) kv.getKey(),bindFunction);
            }
        }

        return classIntance;
    }

}
