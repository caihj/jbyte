try:
    print "start"
    raise UserWarning("hello",1,2,3,5)
except Exception, e:
    print "Exception",e