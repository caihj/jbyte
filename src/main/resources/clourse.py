#coding:utf-8

def foo():
    m=3
    n=5
    def bar():
        a=4
        print m,n
        return m+n+a
    return bar

c=foo()
print(c())