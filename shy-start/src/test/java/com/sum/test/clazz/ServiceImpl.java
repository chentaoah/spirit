package com.sum.test.clazz;

class ServiceImpl extends AbsService {

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

}
