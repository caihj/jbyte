package com.ff.vm.real;

/**
 * Created by caihaijun@navercorp.com on 2018/5/25.
 */
public class Block {
    int toAddress;
    Type type;

    public static enum Type{
        TRY, LOOP
    }
}
