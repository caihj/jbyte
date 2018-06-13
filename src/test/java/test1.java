import com.ff.vm.toy.Code;
import com.ff.vm.toy.VirtualMachine;
import org.javatuples.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

/**
 * Created by chjun1991@163.com on 2018/5/18.
 */

@RunWith(JUnit4.class)
public class test1 {

    @Test
    public void testOne(){

        Code c = new Code();
        c.instructions = Arrays.asList(
                Pair.with("LOAD_VALUE",0),
                Pair.with("LOAD_VALUE",1),
                Pair.with("ADD_TWO_VALUES",null),
                Pair.with("PRINT_ANSWER",null)
        );
        c.numbers = Arrays.asList(7,5);

        VirtualMachine vm = new VirtualMachine();
        vm.run_code(c);
    }


    @Test
    public void testTwo(){

        Code c = new Code();
        c.instructions = Arrays.asList(
                Pair.with("LOAD_VALUE",0),
                Pair.with("LOAD_VALUE",1),
                Pair.with("ADD_TWO_VALUES",null),
                Pair.with("LOAD_VALUE",2),
                Pair.with("ADD_TWO_VALUES",null),
                Pair.with("PRINT_ANSWER",null)
        );
        c.numbers = Arrays.asList(7,5,8);

        VirtualMachine vm = new VirtualMachine();
        vm.run_code(c);
    }

    @Test
    public void testThree(){

        Code c = new Code();
        c.instructions = Arrays.asList(
                Pair.with("LOAD_VALUE",0),
                Pair.with("STORE_NAME",0),
                Pair.with("LOAD_VALUE",1),
                Pair.with("STORE_NAME",1),
                Pair.with("LOAD_NAME",0),
                Pair.with("LOAD_NAME",1),
                Pair.with("ADD_TWO_VALUES",null),
                Pair.with("PRINT_ANSWER",null)
        );
        c.numbers = Arrays.asList(1,2);
        c.names = Arrays.asList("a","b");

        VirtualMachine vm = new VirtualMachine();
        vm.run_code(c);
        int cf = 1024 * 1024 * 1024 ;
        System.out.println(cf);

    }
}
