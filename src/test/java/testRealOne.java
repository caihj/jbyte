import com.ff.vm.real.Code;
import com.ff.vm.real.VirtualMachine;
import com.ff.vm.real.VirtualMachineStatic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by caihaijun@navercorp.com on 2018/5/18.
 */

@RunWith(JUnit4.class)
public class testRealOne {

    @Test
    public void testbasic1(){

        /**
         * python code:
         x=1
         print(x)
         */
        Code code = new Code();


        code.co_code = new short[]{0x64,0x00,0x00,0x5a,0x00,0x00,0x65,0x00,0x00,0x47,0x48,0x64,0x01,0x00,0x53};
        code.co_consts = new Object[]{1, VirtualMachineStatic.None};
        code.co_names = new String[]{"x"};



        VirtualMachine vm = new VirtualMachine();
        vm.run_code(code);

    }

    @Test
    public void testByte(){

        byte b = 50;
        System.out.println((int)b<<2);
    }
}
