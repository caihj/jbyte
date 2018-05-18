package com.ff.vm.real;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by caihaijun@navercorp.com on 2018/5/18.
 */
public class VirtualMachineStatic {


    //see opcode.py
    public static final int HAS_ARGUMENT = 90;

    //warning ,the set type ,because of java template erase,so the result type is Set<Integer>,
    public static Set<Integer> hasconst = new TreeSet(Arrays.asList(100));

    public static Set<Integer> hasname = new TreeSet(Arrays.asList(90,91,95,96,97,98,101,106,108,109,116));

    public static Set<Integer> hasfree = new TreeSet(Arrays.asList(135,136,137));

    public static Set<Integer> hasjrel = new TreeSet(Arrays.asList(93,110,120,121,122,143));

    public static Set<Integer> hasjabs = new TreeSet(Arrays.asList(111,112,113,114,115,119));

    public static Set<Integer> haslocal = new TreeSet(Arrays.asList(124,125,126));

    public static Object None = new Object();

    public static String [] op = new String[256];

    public static String opcodeTostr(int opcode){
        return op[opcode];
    }


    static {
        op[0] = "STOP_CODE";
        op[1] = "POP_TOP";
        op[2] = "ROT_TWO";
        op[3] = "ROT_THREE";
        op[4] = "DUP_TOP";
        op[5] = "ROT_FOUR";
        op[9] = "NOP";
        op[10] = "UNARY_POSITIVE";
        op[11] = "UNARY_NEGATIVE";
        op[12] = "UNARY_NOT";
        op[13] = "UNARY_CONVERT";
        op[15] = "UNARY_INVERT";
        op[19] = "BINARY_POWER";
        op[20] = "BINARY_MULTIPLY";
        op[21] = "BINARY_DIVIDE";
        op[22] = "BINARY_MODULO";
        op[23] = "BINARY_ADD";
        op[24] = "BINARY_SUBTRACT";
        op[25] = "BINARY_SUBSCR";
        op[26] = "BINARY_FLOOR_DIVIDE";
        op[27] = "BINARY_TRUE_DIVIDE";
        op[28] = "INPLACE_FLOOR_DIVIDE";
        op[29] = "INPLACE_TRUE_DIVIDE";
        op[30] = "SLICE+0";
        op[31] = "SLICE+1";
        op[32] = "SLICE+2";
        op[33] = "SLICE+3";
        op[40] = "STORE_SLICE+0";
        op[41] = "STORE_SLICE+1";
        op[42] = "STORE_SLICE+2";
        op[43] = "STORE_SLICE+3";
        op[50] = "DELETE_SLICE+0";
        op[51] = "DELETE_SLICE+1";
        op[52] = "DELETE_SLICE+2";
        op[53] = "DELETE_SLICE+3";
        op[54] = "STORE_MAP";
        op[55] = "INPLACE_ADD";
        op[56] = "INPLACE_SUBTRACT";
        op[57] = "INPLACE_MULTIPLY";
        op[58] = "INPLACE_DIVIDE";
        op[59] = "INPLACE_MODULO";
        op[60] = "STORE_SUBSCR";
        op[61] = "DELETE_SUBSCR";
        op[62] = "BINARY_LSHIFT";
        op[63] = "BINARY_RSHIFT";
        op[64] = "BINARY_AND";
        op[65] = "BINARY_XOR";
        op[66] = "BINARY_OR";
        op[67] = "INPLACE_POWER";
        op[68] = "GET_ITER";
        op[70] = "PRINT_EXPR";
        op[71] = "PRINT_ITEM";
        op[72] = "PRINT_NEWLINE";
        op[73] = "PRINT_ITEM_TO";
        op[74] = "PRINT_NEWLINE_TO";
        op[75] = "INPLACE_LSHIFT";
        op[76] = "INPLACE_RSHIFT";
        op[77] = "INPLACE_AND";
        op[78] = "INPLACE_XOR";
        op[79] = "INPLACE_OR";
        op[80] = "BREAK_LOOP";
        op[81] = "WITH_CLEANUP";
        op[82] = "LOAD_LOCALS";
        op[83] = "RETURN_VALUE";
        op[84] = "IMPORT_STAR";
        op[85] = "EXEC_STMT";
        op[86] = "YIELD_VALUE";
        op[87] = "POP_BLOCK";
        op[88] = "END_FINALLY";
        op[89] = "BUILD_CLASS";
        op[90] = "STORE_NAME";
        op[91] = "DELETE_NAME";
        op[92] = "UNPACK_SEQUENCE";
        op[93] = "FOR_ITER";
        op[94] = "LIST_APPEND";
        op[95] = "STORE_ATTR";
        op[96] = "DELETE_ATTR";
        op[97] = "STORE_GLOBAL";
        op[98] = "DELETE_GLOBAL";
        op[99] = "DUP_TOPX";
        op[100] = "LOAD_CONST";
        op[101] = "LOAD_NAME";
        op[102] = "BUILD_TUPLE";
        op[103] = "BUILD_LIST";
        op[104] = "BUILD_SET";
        op[105] = "BUILD_MAP";
        op[106] = "LOAD_ATTR";
        op[107] = "COMPARE_OP";
        op[108] = "IMPORT_NAME";
        op[109] = "IMPORT_FROM";
        op[110] = "JUMP_FORWARD";
        op[111] = "JUMP_IF_FALSE_OR_POP";
        op[112] = "JUMP_IF_TRUE_OR_POP";
        op[113] = "JUMP_ABSOLUTE";
        op[114] = "POP_JUMP_IF_FALSE";
        op[115] = "POP_JUMP_IF_TRUE";
        op[116] = "LOAD_GLOBAL";
        op[119] = "CONTINUE_LOOP";
        op[120] = "SETUP_LOOP";
        op[121] = "SETUP_EXCEPT";
        op[122] = "SETUP_FINALLY";
        op[124] = "LOAD_FAST";
        op[125] = "STORE_FAST";
        op[126] = "DELETE_FAST";
        op[130] = "RAISE_VARARGS";
        op[131] = "CALL_FUNCTION";
        op[132] = "MAKE_FUNCTION";
        op[133] = "BUILD_SLICE";
        op[134] = "MAKE_CLOSURE";
        op[135] = "LOAD_CLOSURE";
        op[136] = "LOAD_DEREF";
        op[137] = "STORE_DEREF";
        op[140] = "CALL_FUNCTION_VAR";
        op[141] = "CALL_FUNCTION_KW";
        op[142] = "CALL_FUNCTION_VAR_KW";
        op[143] = "SETUP_WITH";
        op[145] = "EXTENDED_ARG";
        op[146] = "SET_ADD";
        op[147] = "MAP_ADD";
    }


}
