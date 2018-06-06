package com.ff.vm.real;

import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyDict;
import com.ff.vm.real.type.basic.PyStr;
import com.ff.vm.real.type.constant.BasicConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        int a=0;
        Map<PyStr,PyObject> local = new HashMap<>();

        for(int i=args.size()-1;i>=0 && a<code.co_varnames.value.length;i--){
            local.put((PyStr) code.co_varnames.value[a++],args.get(i));
        }

        for(;a<code.co_varnames.value.length;a++){
            PyStr name = (PyStr) code.co_varnames.value[a];
            PyObject v = kw.__subscr__(name);
            if(v==null){
                //argument not send;
                throw new RuntimeException("bad argument");
            }
            local.put(name ,v);
            kw.__delsubscr__(name);
        }

        if(kw.__len__().value!=0){
            throw new RuntimeException("argument not match "+ kw.toString());
        }
        a=0;
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
