package com.ff.vm.real.builtin;

import com.ff.vm.real.BuiltInFunction;
import com.ff.vm.real.VirtualMachine;
import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyBool;
import com.ff.vm.real.type.basic.PyDict;
import com.ff.vm.real.type.basic.PyStr;
import com.ff.vm.real.util.CommonUtil;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chjun1991@163.com on 2018/5/31.
 */
public class UserWarning extends BaseException  {


    public UserWarning(List<PyObject> msgObj) {
        super(msgObj);
    }

    @Override
    public String type() {
        return "UserWarning";
    }
}
