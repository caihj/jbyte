#coding:utf-8

class foo():
    def __init__(self,a):
        self.a=a
        self.b=2
    def show(self):
        print self.a


class Too(foo):
    def __init__(self,a):
        foo.__init__(self,a)

f=foo(2)
d=foo(3)
f.show()
d.show()

q=Too(6)
q.show()

if q < f:
    print "sss"
