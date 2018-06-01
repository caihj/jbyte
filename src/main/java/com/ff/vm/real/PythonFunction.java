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

        for(int i=args.size()-1;i>=0;i--){
            local.put((PyStr) code.co_varnames.value[a++],args.get(i));
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
