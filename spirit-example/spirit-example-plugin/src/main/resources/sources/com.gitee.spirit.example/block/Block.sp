
//代码块
class Block {

	s="hello"
	
	func testIf(){
		if s=="hello"{
			print s
		}
		if s!="hello"{
			print s
		}
		if empty(s) {
			print s
		}
		if !empty(s) {
			print s
		}
		
		if s : print s
		
		if s : print s : print "yes"
	}
	
	func testFor(){
		//遍历Map
		map={"key":100,"key":100}
		for key in map.keySet(){
			print key
		}
		for value in map.values(){
			print "number is {}",value
		}
		for entry in map.entrySet(){
			print "test entry!",entry.getKey()
		}
		
		//遍历List
		list=["first","second","third"]
		for str in list : print str : break
		
		//索引遍历
		for (i=0; i<list.size(); i++) {
			s=list.get(i)
			print "thank {} very much!", "you"
			continue
		}
		
		//索引遍历
		for (i=0; i<list.size(); i++) : s=list.get(i) : s=String.valueOf(12345) : s="caixukun" : continue
		
		//遍历数组
		nums=int[100]
		for num in nums : print "num is {}",num
		
	}
	
	func testWhile(){
		y="hi!"
		while y {
			print y
			break
		}
	}
	
	func testTry(){
		try{
			if s=="hello" : throw Exception("test")
			return s
		}catch Exception e{
			error "error is",e
		}finally{
			print "hello"
		}
		return null
	}
	
	synch func testSync(){
		sync s {
			print "in sync!"
		}
	}
	
	func testThrows() throws Exception {
		return s
	}

	func testGetLines(){
		//测试转义对字符串的影响
		if s=="hello\\" {
			print "test"
		}
		//测试行截取
		if s=="hello" {
			print "test}"
		}
	}



}
