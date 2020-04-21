package com.sum.test.clazz;

import com.sum.test.clazz.AbsService;

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

}
