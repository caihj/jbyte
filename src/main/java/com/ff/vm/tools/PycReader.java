package com.ff.vm.tools;

import com.ff.vm.real.Code;
import com.ff.vm.tools.marshal.ByteReader;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

@Slf4j
public class PycReader {

    public Code readFile(String fileName) throws IOException {

        Code code = null;

        FileInputStream inputStream =  new FileInputStream(fileName);

        byte  [] magic = new byte[4];
        inputStream.read(magic);
        log.info("magic {}",byteToHex(magic));
        byte []  timeStamp = new byte[4];
        inputStream.read(timeStamp);
        log.info(new Date(fourByteToLong(timeStamp)*1000).toString());

        ByteReader reader = new ByteReader(inputStream);
        code = (Code) reader.readObject();

        return code;
    }

    public static String byteToHex(byte[] arr){
        StringBuilder sb = new StringBuilder(arr.length*2);

        for(byte b:arr){
            sb.append(String.format("%02x",b));
        }
        return sb.toString();
    }

    /**
     * arr little edition
     * @param arr
     * @return
     */
    public static Long fourByteToLong(byte [] arr){
        long l=0;

        l = (arr[0] & 0xff) + ((0xff & arr[1])<<8) + ((0xff & arr[2])<<16) + ((0xff & arr[3])<<24);
        return l;
    }

}
