package com.ff.vm.real;

/**
 * Created by chjun1991@163.com on 2018/5/25.
 */
public class Block {
    int toAddress;
    Type type;

    public static enum Type{
        TRY, LOOP
    }
}
