package com.ff.vm.real;

import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyStr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caihaijun@navercorp.com on 2018/5/28.
 */
public class PythonFunction extends Function {
    @Override
    public PyObject call(VirtualMachine vm,List<PyObject> args) {
        int a=0;
        Map<PyStr,PyObject> local = new HashMap<>();
        for(PyObject key:code.co_varnames.value){
            local.put((PyStr) key,args.get(args.size()-(++a)));
        }

        Frame newFrame = new Frame(code,vm.curFrame().global_names,local,vm.curFrame());
        PyObject returnValue = vm.run_frame(newFrame);
        return returnValue;
    }
}
