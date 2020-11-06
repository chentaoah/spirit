package com.sum.test.clazz;

public class ServiceImpl<T, K> extends AbsService {

	public T key;
	public K value;
	public String name;

	public ServiceImpl() {
		this("chentao");
	}

	public ServiceImpl(String name) {
		super();
		this.name = name;
	}

	public String one() {
		return "one";
	}

	public int two() {
		return 10;
	}

	public byte[] three() {
		return new byte[10];
	}

	public K testT(T t) {
		return value;
	}

	public String testAbsMethod(String test) {
		return "success";
	}

	public T testReturnGenericType(T t) {
		return t;
	}

	public String test1(T t) {
		return "hello";
	}

	public int test1(int number) {
		return 1;
	}

}
