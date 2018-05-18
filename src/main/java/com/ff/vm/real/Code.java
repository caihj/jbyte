package com.ff.vm.real;

/**
 * Created by caihaijun@navercorp.com on 2018/5/18.
 */
public class Code {

    //because of in java, byte means signed byte
    //so we use short to represent  byte from 0x00~0xff, and it's all positive number.
    public short [] co_code ;

    //const
    public Object []  co_consts;

    //names
    public String [] co_names;

    public String[] co_varnames;

    public Object[] co_cellvars;

    public Object[] co_freevars;

}
