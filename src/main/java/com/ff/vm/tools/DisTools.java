package com.ff.vm.tools;

import com.ff.vm.real.Code;
import com.ff.vm.real.type.PyObject;
import com.ff.vm.real.type.basic.PyStr;
import org.javatuples.Triplet;

import static com.ff.vm.real.VirtualMachineStatic.*;
import static com.ff.vm.real.VirtualMachineStatic.opcodeTostr;

/**
 * Created by chjun1991@163.com on 2018/5/24.
 */
public class DisTools {

    public static String dis(Code code){

        StringBuilder sb = new StringBuilder();
//        for(PyObject obj:code.co_names.value){
//            sb.append("names:"+obj+"\n");
//        }
//        for(PyObject obj:code.co_varnames.value){
//            sb.append("vname:"+obj+"\n");
//        }
        sb.append("#############\n");
        sb.append("name:"+code.name+"\n");
        sb.append(code+"\n");

        int i = 0;

        while(i < code.co_code.value.length){

            int b = 0xff & (code.co_code.value[i]);

            int next;

            Object argObj = null;

            if(b>=HAS_ARGUMENT){
                next = i + 3;
                int arg = (short) (( 0xff & code.co_code.value[i+1]) + ((0xff & code.co_code.value[i+2])<<8));
                if(hasconst.contains((int)b)){
                    argObj = code.co_consts.value[arg];
                }else if(hasfree.contains(b)){

                    if(arg<code.co_cellvars.value.length){
                        argObj = code.co_cellvars.value[arg];
                    }else{
                        int var_idx = arg - code.co_cellvars.value.length;
                        argObj = code.co_freevars.value[var_idx];
                    }
                }else if(hasname.contains(b)){
                    argObj = code.co_names.value[arg];
                }else if(hasjrel.contains(b)){
                    argObj =  arg + " to ("+ (arg + next ) +")";
                }else if(hasjabs.contains(b)){
                    argObj = arg + " to ("+ (arg ) +")";
                }else if(haslocal.contains(b)){
                    argObj = code.co_varnames.value[arg];
                }else{
                    argObj = arg;
                }

            }else{
                next = i + 1;
            }

            Triplet<String,PyObject,Integer> op = new Triplet(opcodeTostr(b),argObj,b);

            if(op.getValue1() instanceof Code){
//                sb.append(dis((Code) op.getValue1()));

                sb.append(i+"\t"+op.getValue0()+"\t"+(op.getValue1()==null ? "" : w(op.getValue1())) +"\n");
            }else{
                sb.append(i+"\t"+op.getValue0()+"\t"+(op.getValue1()==null ? "" : w(op.getValue1()) ) +"\n");
            }

            i = next;

        }

        sb.append("==========\n");

        for(Object c:code.co_consts.value){
            if (c instanceof Code){
                sb.append(dis((Code) c));
            }
        }


        return sb.toString();

    }

    public static Object w(Object o){
         if (o instanceof PyStr){
             return "\""+o.toString()+"\"";
        }else {
             return o;
         }
    }
}
