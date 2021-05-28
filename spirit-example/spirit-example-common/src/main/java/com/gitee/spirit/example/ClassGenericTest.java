package com.gitee.spirit.example;

@MyTest
public class ClassGenericTest {

	public Class<?> getClazz() {
		return null;
	}

	public void getAnno() {
		MyTest myTest = ClassGenericTest.class.getAnnotation(MyTest.class);
		System.out.println(myTest);
	}

}
