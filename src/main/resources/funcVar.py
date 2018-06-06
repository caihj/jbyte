#coding:utf-8

def foo(a,b,c):
    print a,b,c

foo(1,*(2,3))
foo(1,*[2,3])

