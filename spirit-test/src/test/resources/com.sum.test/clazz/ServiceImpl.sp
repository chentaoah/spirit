
class ServiceImpl<T,K> extends AbsService{

	T key

	K value

	String name
	//测试构造方法
	func ServiceImpl(){
		this("chentao")
	}

	func ServiceImpl(String name){
		super()
		this.name=name
	}

	func one(){
		return "one"
	}
	
	func two(){
		return 10
	}
	
	func three(){
		return byte[10]
	}

	func testT(T t){
		return value
	}

	func testAbsMethod(String test){
		return "success"
	}

	T testReturnGenericType(T t){
		return t
	}

	func test1(T t){
		return "hello"
	}

	func test1(int number){
		return 1
	}

}