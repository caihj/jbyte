#coding:utf-8


def test_var_args_call(arg1, arg2, arg3):
    print "arg1:", arg1
    print "arg2:", arg2
    print "arg3:", arg3



kwargs = {"arg3": 3, "arg2": "two"}
test_var_args_call(1, **kwargs)