package com.ff.vm.real;

import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyDict;
import com.ff.vm.real.type.basic.PyList;
import com.ff.vm.real.type.basic.PyStr;
import com.ff.vm.real.type.constant.BasicConstant;

import java.util.*;

/**
 * Created by caihaijun@navercorp.com on 2018/5/28.
 */
public class PythonFunction extends Function {

    @Override
    public PyObject call(VirtualMachine vm,List<PyObject> args) {
        return doCall(vm,args,null);
    }

    @Override
    public  PyObject call(VirtualMachine vm, List<PyObject> args, PyDict kw){
        return doCall(vm,args,kw);
    }

    private PyObject doCall(VirtualMachine vm,List<PyObject> args, PyDict kw) {
        Map<PyStr,PyObject> local = new HashMap<>();

        int argcount = (int) code.argcount.value;

        int argIdx = 0;
        for(;argIdx<argcount && argIdx< args.size();argIdx++){
            local.put((PyStr) code.co_varnames.value[argIdx],args.get(argIdx));
        }
        args = args.subList(argIdx,args.size());

        for(;argIdx<argcount;argIdx++){
            PyStr varName = (PyStr) code.co_varnames.value[argIdx];
            local.put(varName,kw.value.get(varName));
            kw.value.remove(varName);
        }

        if((code.flags.value & 0x04)!=0){
            local.put((PyStr) code.co_varnames.value[argIdx++],new PyList(args));
            args = Collections.emptyList();
        }

        if((code.flags.value & 0x08)!=0){
            local.put((PyStr) code.co_varnames.value[argIdx++],kw);
            kw = new PyDict();
        }
        if(args.size()!=0 || kw.value.size()!=0){
            throw new RuntimeException("argument error");
        }


        int a=0;
        Map<PyStr,Cell> cellMap = new HashMap<>();

        for(int i=cells.value.length-1;i>=0;i--){

            PyObject c = cells.value[i];
            int idx =a++;
            PyObject argObj;

            if(idx<code.co_cellvars.value.length){
                argObj = code.co_cellvars.value[idx];
            }else{
                int var_idx = idx - code.co_cellvars.value.length;
                argObj = code.co_freevars.value[var_idx];
            }

            cellMap.put((PyStr) argObj, (Cell) c);
        }


        Frame newFrame = new Frame(code,vm.curFrame().global_names,local,vm.curFrame(),cellMap);
        PyObject returnValue = vm.run_frame(newFrame);
        return returnValue;
    }



}
