import com.ff.vm.tools.PycReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sun.misc.ClassLoaderUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@RunWith(JUnit4.class)
public class TestByte {

    @Test
    public void test(){

        byte [] arr = new byte[4];

        arr[0]  = 0x26;
        arr[1] = 0x0a;
        arr[2] = 0x00;
        arr[3] = 0x5b;

        long d = PycReader.fourByteToLong(arr);
        System.out.println(new Date(d*1000));
    }

    @Test
    public void testFileReader() throws IOException {

        PycReader reader = new PycReader();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("basic.pyc").getPath());
        reader.readFile(file.getPath());
    }
}
