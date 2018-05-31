#coding:utf-8


import dis
import marshal
import sys



def disCode(fileName):
    fp=open(fileName,"rb")
    fp.read(8)
    code = marshal.load(fp)
    print dis.dis(code)


if __name__=='__main__':
    print sys.argv
    disCode(sys.argv[1])