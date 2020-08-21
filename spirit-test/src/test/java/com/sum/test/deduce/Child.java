package com.sum.test.deduce;

import com.sum.spirit.test.example.ClassGenericTest;
import com.sum.test.deduce.Father;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Child extends Father {

	public static final Logger logger = LoggerFactory.getLogger(Child.class);
	public Father father = new Father();
	public int age = 18;
	public ClassGenericTest t = new ClassGenericTest();

	public String sayHello() {
		return super.sayHello();
	}

	public Father getFather() {
		return father;
	}

	public String testMembers() {
		String a = getFather().getChild().getFather().name;
		String b = father.child.father.child.father.name;
		Child c = father.getChild();
		logger.info("test members {} {}", a, b, c);
		return this.sayHello();
	}

	public String testClassGeneric() {
		Class<?> a = t.getClazz();
		return a.getName();
	}

}
