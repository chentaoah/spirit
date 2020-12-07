
//主类
class Outside {

	f1=100
	f2=100.01
	func getName(){
		return "I am Outside"
	}

	func getF1(){
		return f1
	}

	func testInner(){
		inner=Inner()
		return inner.getAge()
	}

}
//测试内部类
class Inner {

	f1="I am Inner!"
	f2="hello world!"

	func getAge(){
		return 18
	}

	func testImport(){
		print Father().toString()
	}

}