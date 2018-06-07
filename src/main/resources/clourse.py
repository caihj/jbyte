#coding:utf-8

def foo():
    m=3
    n=5
    def bar():
        a=4
        print m,n
        def cc():
            return a+m+n
        return cc
    return bar

c=foo()
print(c()())