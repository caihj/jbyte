package com.ff.vm.tools.marshal;

import com.ff.vm.real.Code;
import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.*;
import com.ff.vm.real.type.constant.BasicConstant;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * refer to http://demoseen.com/blog/2010-02-20_Python_Marshal_Format.html
 */
public class ByteReader {

    private InputStream in;

    private int pos = 0;

    private List<PyStr> internedList = new ArrayList<>(10);

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
    public PyInt readInt() throws IOException {
        byte arr[] = new byte[4];
        in.read(arr);
        long  value = (arr[0] & 0xff) + ((0xff & arr[1])<<8) + ((0xff & arr[2])<<16) + ((0xff & arr[3])<<24);
        return new PyInt(value);
    }

    /**
     * byte str
     * @return
     * @throws IOException
     */
    public PyStr readStr() throws IOException {
        PyInt len = readInt();
        byte []  arr = new byte[(int) len.value];
        in.read(arr);
        return new PyStr(arr);
    }

    public PyTuple readTuple() throws IOException {
        PyInt len = readInt();
        PyObject[] tuple = new PyObject[(int) len.value];

        for(int i=0;i<len.value;i++){
            tuple[i] = readObject();
        }
        return  new PyTuple(tuple);
    }

    public PyObject readObject() throws IOException {
        int typeFlag = in.read();
        switch (typeFlag){
            case '0':return BasicConstant.TYPE_NULL;
            case 'N':return BasicConstant.TYPE_NONE;
            case 'F':return BasicConstant.TYPE_FALSE;
            case 'T':return BasicConstant.TYPE_TRUE;
            case 'S':return BasicConstant.TYPE_STOPITER;
            case '.':return BasicConstant.TYPE_ELLIPSIS;
            case 'i':return readInt();
            case 'I':return readLong();
            case 'f':return readFloatStr();
            case 'g':return readFloat();
            case 'x':return readComplex();
            case 'l':return readLong();
            case 's':return readStr();
            case 't':;internedList.add(readStr());return internedList.get(internedList.size()-1);
            case 'R':return internedList.get((int) readInt().value);
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
        code.co_code = (PyStr) readObject();
        code.co_consts = (PyTuple) readObject();
        code.co_names = (PyTuple) readObject();
        code.co_varnames = (PyTuple) readObject();
        code.co_freevars = (PyTuple) readObject();
        code.co_cellvars = (PyTuple) readObject();
        code.filename = (PyStr) readObject();
        code.name = (PyStr)readObject();
        code.firstlineno = readInt();
        code.lnotab = readObject();


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

    private PyDict readDict() {
        throw new UnsupportedOperationException("dict not implement");
    }

    private PyList readList() throws IOException {
        PyInt len = readInt();
        PyObject [] arr = new PyObject[(int) len.value];
        for(int i=0;i<len.value;i++){
            arr[i] = readObject();
        }
        return new PyList(Arrays.asList(arr));
    }

    private PyStr readUtf8Str() throws IOException {
        return  readStr();
    }

    private PyComplex readComplex() throws IOException {
        PyComplex complex = new PyComplex();

        complex.real = Double.valueOf(new String(readFloatStr().value));
        complex.image =  Double.valueOf(new String(readFloatStr().value));
        return complex;
    }

    private PyFloat readFloat() throws IOException {
        double d = Double.longBitsToDouble(readLong().value);
        return new PyFloat(d);
    }

    private PyStr readFloatStr() throws IOException {
        return readStr();
    }

    private PyInt readLong() throws IOException {
        byte [] arr = new byte[8];
        in.read(arr);
        long ret=0;

        for(int i=0;i<8;i++){
            ret += (0xff & arr[i]) <<(8*i);
        }
        return new PyInt(ret);

    }
}
