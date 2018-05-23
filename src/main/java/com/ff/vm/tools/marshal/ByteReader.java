package com.ff.vm.tools.marshal;

import com.ff.vm.real.Code;
import com.ff.vm.tools.marshal.type.Complex;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * refer to http://demoseen.com/blog/2010-02-20_Python_Marshal_Format.html
 */
public class ByteReader {

    private InputStream in;

    private int pos = 0;

    public ByteReader(InputStream in){
        this.in = new InputStream() {
            @Override
            public int read() throws IOException {
                pos++;
                return in.read();
            }

            @Override
            public int read(byte arr[]) throws IOException {
                pos +=arr.length;
                return in.read(arr);
            }
        };
    }
    public int readInt() throws IOException {
        byte arr[] = new byte[4];
        in.read(arr);
        return  (arr[0] & 0xff) + ((0xff & arr[1])<<8) + ((0xff & arr[2])<<16) + ((0xff & arr[3])<<24);
    }

    /**
     * byte str
     * @return
     * @throws IOException
     */
    public byte[] readStr() throws IOException {
        int len = readInt();
        byte []  arr = new byte[len];
        in.read(arr);
        return arr;
    }

    public Object[] readTuple() throws IOException {
        int len = readInt();
        Object [] tuple = new Object[len];

        for(int i=0;i<len;i++){
            tuple[i] = readObject();
        }
        return tuple;
    }

    public Object readObject() throws IOException {
        int typeFlag = in.read();
        switch (typeFlag){
            case '0':return Constants.TYPE_NULL;
            case 'N':return Constants.TYPE_NONE;
            case 'F':return Constants.TYPE_FALSE;
            case 'T':return Constants.TYPE_TRUE;
            case 'S':return Constants.TYPE_STOPITER;
            case '.':return Constants.TYPE_ELLIPSIS;
            case 'i':return readInt();
            case 'I':return readLong();
            case 'f':return readFloatStr();
            case 'g':return readFloat();
            case 'x':return readComplex();
            case 'l':return readLong();
            case 's':return readStr();
            case 't':return readStr();
            case 'R':return readInt();
            case 'u':return readUtf8Str();
            case '(':return readTuple();
            case '[':return readList();
            case '{':return readDict();
            case '>':return readTuple();

            case 'c':return readCode();
            default:
                System.out.println(pos);
                throw new RuntimeException("bad type " + Character.toString((char) typeFlag) );
        }

    }

    private Code readCode() throws IOException {
        Code code = new Code();
        code.argcount = readInt();
        code.nlocals = readInt();
        code.stacksize = readInt();
        code.flags = readInt();
        code.co_code = (byte[]) readObject();
        code.co_consts = (Object[]) readObject();
        code.co_names =  convertToStrArr((Object[]) readObject());
        code.co_varnames = convertToStrArr((Object[]) readObject());
        code.co_freevars = (Object[]) readObject();
        code.co_cellvars = (Object[]) readObject();
        code.filename = new String((byte[]) readObject(),"utf-8");

        return code;
    }

    public String []  convertToStrArr(Object[] arr){
        String [] str= new String[arr.length];
        for(int i=0;i<arr.length;i++){
            try {
                str[i] = new String((byte[]) arr[i],"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    private Object readDict() {
        return null;
    }

    private Object readList() throws IOException {
        int len = readInt();
        Object [] arr = new Object[len];
        for(int i=0;i<len;i++){
            arr[i] = readObject();
        }
        return arr;
    }

    private String readUtf8Str() throws IOException {

        byte [] arr = readStr();
        return new String(arr,"utf-8");
    }

    private Complex readComplex() throws IOException {
        Complex complex = new Complex();

        complex.real = readFloatStr();
        complex.image = readFloatStr();
        return complex;
    }

    private double readFloat() throws IOException {
        double d = Double.longBitsToDouble(readLong());
        return d;
    }

    private String readFloatStr() throws IOException {
        String floatStr = new String(readStr());
        return floatStr;
    }

    private long readLong() throws IOException {
        byte [] arr = new byte[8];
        in.read(arr);
        long ret=0;

        for(int i=0;i<8;i++){
            ret += (0xff & arr[i]) <<(8*i);
        }
        return ret;

    }



}
