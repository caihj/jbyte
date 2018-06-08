import com.ff.vm.real.Code;
import com.ff.vm.real.VirtualMachine;
import com.ff.vm.real.VirtualMachineStatic;
import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyInt;
import com.ff.vm.real.type.basic.PyList;
import com.ff.vm.real.type.basic.PyStr;
import com.ff.vm.real.type.basic.PyTuple;
import com.ff.vm.real.type.constant.BasicConstant;
import com.ff.vm.tools.DisTools;
import com.ff.vm.tools.PycReader;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Target;

/**
 * Created by chjun1991@163.com on 2018/5/18.
 */

@RunWith(JUnit4.class)
public class testRealOne {

    @BeforeClass
    public static  void setUp(){
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        try {
            String path = testRealOne.class.getResource("compile.bat").getPath();
            String [] cmd={"cmd","/c",path.substring(1,path.length())};
            Process p = Runtime.getRuntime().exec(cmd);
            InputStream i = p.getInputStream();
            int c;
            while(( c=i.read())>0){
                System.out.print((char)c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testbasic1(){

        /**
         * python code:
         x=1
         print(x)
         */
        Code code = new Code();


        code.co_code = new PyStr(new byte[]{0x64,0x00,0x00,0x5a,0x00,0x00,0x65,0x00,0x00,0x47,0x48,0x64,0x01,0x00,0x53});
        code.co_consts = new PyTuple(new PyObject[]{new PyInt(1), BasicConstant.TYPE_NONE});
        code.co_names = new PyTuple(new PyObject[]{new PyStr("x")});

        VirtualMachine vm = new VirtualMachine();
        vm.run_code(code);

    }

    private void runFile(String fileName) throws IOException, InterruptedException {
        PycReader reader = new PycReader();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getPath());
        Code code = reader.readFile(file.getPath());
        System.out.println(DisTools.dis(code));
        System.out.flush();
        Thread.sleep(100);

        VirtualMachine vm = new VirtualMachine();
        try {
            vm.run_code(code);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testBasic() throws Exception {
        runFile("basic.pyc");
    }

    @Test
    public void testfor() throws Exception {
        runFile("for_range.pyc");
    }

    @Test
    public void testFunction() throws Exception {
        runFile("function.pyc");
    }

    @Test
    public void testloop() throws Exception {
        runFile("loop.pyc");
    }

    @Test
    public void functionReturn() throws Exception {
        runFile("functionReturn.pyc");
    }
    @Test
    public void hello() throws Exception {
        runFile("hello.pyc");
    }

    @Test
    public void tryCatch() throws Exception {
        runFile("TryCatch.pyc");
    }

    @Test
    public void testCloure() throws IOException, InterruptedException {
        runFile("clourse.pyc");
    }

    @Test
    public void dictTest() throws IOException, InterruptedException {
        runFile("dictTest.pyc");
    }

    @Test
    public void funcVarTest() throws IOException, InterruptedException {
        runFile("funcVar.pyc");
    }

    @Test
    public void funcKwTest() throws IOException, InterruptedException {
        runFile("funcKw.pyc");
    }
    @Test
    public void kwTestTest() throws IOException, InterruptedException {
        runFile("kwTest.pyc");
    }

    @Test
    public void classTest() throws IOException, InterruptedException {
        runFile("class.pyc");
    }

    @Test
    public void tryCatch2() throws Exception {
        runFile("TryCatch2.pyc");
    }

}
