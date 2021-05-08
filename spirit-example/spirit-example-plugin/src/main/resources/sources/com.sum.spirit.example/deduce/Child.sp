
import com.sum.spirit.example.ClassGenericTest

class Child extends Father{

	father=Father()
	age=18
	t=ClassGenericTest()

	func sayHello(){
		return super.sayHello()
	}

	func getFather(){
		return father
	}

	func testMembers(){
		a=getFather().getChild().getFather().name
		b=father.child.father.child.father.name
		c=father.getChild()
		print "test members {} {}",a,b,c
		return this.sayHello()
	}

	func testClassGeneric(){
		a = t.getClazz()
		return a.getName()
	}





}