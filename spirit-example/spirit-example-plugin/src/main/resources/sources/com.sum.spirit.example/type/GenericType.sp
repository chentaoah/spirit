
import java.util.HashMap

@SuppressWarnings("serial")
class GenericType<T,K> extends HashMap<T,Integer> {

	func testGeneric(){
		return get("test")
	}

	func test(){
		return HashMap<T,K>()
	}

	func test1(){
		g=GenericType<String,String>()
		return g.get("test")
	}

	func test2(){
		g=GenericType<String,String>()
		return g.test()
	}

	func testB(){
		clazz=int.class
		print clazz.getName()
	}

}