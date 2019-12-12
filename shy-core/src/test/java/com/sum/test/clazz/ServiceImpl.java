package com.sum.test.clazz;

import com.sum.test.clazz.AbsService;

public class ServiceImpl extends AbsService {

	public ServiceImpl() {
		super();
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

	public Class<ServiceImpl> testClass() {
		return ServiceImpl.class;
	}

}
