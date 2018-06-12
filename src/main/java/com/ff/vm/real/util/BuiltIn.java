package com.ff.vm.real.util;

import com.ff.vm.real.Code;
import com.ff.vm.real.Frame;
import com.ff.vm.real.VirtualMachine;
import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.*;
import com.ff.vm.tools.PycReader;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chjun1991@163.com on 2018/5/25.
 */
public class BuiltIn {

    public static PyObject __import__(VirtualMachine vm,PyStr name, Map<PyStr, PyObject> global_names,
                                      Map<PyStr, PyObject> local_names, PyObject level, PyObject fromlist) {

        PycReader reader = new PycReader();
        Code code = null;
        try {
            code = reader.readFile(vm.libDir + new String(name.value)+".pyc");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //execute the code
        Map<PyStr,PyObject> global= new HashMap<>();
        global.put(new PyStr("__name__"),name);
        Frame frame = new Frame(code, global, Collections.EMPTY_MAP,VirtualMachine.builtInConstants,null,null);
        //Frame newFrame = new Frame(code,vm.curFrame().global_names,local,vm.curFrame().builtIn,vm.curFrame(),cellMap);
        Frame ret = vm.import_run_frame(frame);

        return new PyModule(global);
    }
}
