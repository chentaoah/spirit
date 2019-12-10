package com.sum.test.derive;

import com.sum.test.derive.AbsService;

public class Service extends AbsService {

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
