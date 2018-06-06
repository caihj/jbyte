#coding:utf-8


def test_var_args_call(arg1, arg2, arg3):
    print "arg1:", arg1
    print "arg2:", arg2
    print "arg3:", arg3



kwargs = { "arg2": "two","arg1":2}
test_var_args_call(arg3=1, **kwargs)


def test_var_kwargs(farg, **kwargs):
    print "formal arg:", farg
    for key in kwargs:
        print "another keyword arg: %s: %s" % (key, kwargs[key])

test_var_kwargs( myarg2="two", myarg3=3,farg=1)