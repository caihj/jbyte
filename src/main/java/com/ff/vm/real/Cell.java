package com.ff.vm.real;

import com.ff.vm.real.type.PyObject;

/**
 * Created by caihaijun@navercorp.com on 2018/5/31.
 */
public class Cell extends PyObject {
    private PyObject content;

    public PyObject getContent() {
        return content;
    }

    public void setContent(PyObject content) {
        this.content = content;
    }

    @Override
    public String type() {
        return "cell";
    }
}
