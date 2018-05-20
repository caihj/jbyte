package com.ff.vm.tools.marshal;

import com.ff.vm.real.Code;
import sun.nio.ch.ChannelInputStream;

import java.io.IOException;
import java.io.InputStream;

public class Marshal {

    Code unMarshalCode(InputStream in) throws IOException {
        Code code = new Code();

        ByteReader reader = new ByteReader(in);





        return code;
    }

}
