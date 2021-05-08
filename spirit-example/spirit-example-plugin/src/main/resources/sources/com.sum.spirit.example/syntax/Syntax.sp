
//特殊语法
class Syntax {

	list=["first","second"]
	map={"key":100,"key":100}
	
	//一般判断
	func testJudge(){
		b=list.get(1) != null
		bb=list instanceof Object
		if b && bb : print "yes"
		if list.get(1)!=null && list instanceof Object : print "test success"
	}
	
	//异常处理
	func testLog(){
		try{
			print "test print keyword"
			debug "test debug keyword"
			throw Exception("test")
		}catch Exception e{
			error "There is a Exception!",e
		}
	}

}


