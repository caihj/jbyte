#coding:utf-8


import dis
import marshal
import sys


def printCode(codes):
    print '<code object %s at %019x, file %s ,line %s>' % (codes.co_name,id(codes),codes.co_filename,codes.co_firstlineno)
    print dis.dis(codes)
    for c in codes.co_consts:
        if type(c).__name__ == 'code':
            printCode(c)


def disCode(fileName):
    fp=open(fileName,"rb")
    fp.read(8)
    code = marshal.load(fp)
    printCode(code)


if __name__=='__main__':
    disCode(sys.argv[1])