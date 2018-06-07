#coding:utf-8

class foo():
    def __init__(self,a):
        self.a=a
        self.b=2
    def show(self):
        print self.a

f=foo(2)
d=foo(3)
f.show()
d.show()
