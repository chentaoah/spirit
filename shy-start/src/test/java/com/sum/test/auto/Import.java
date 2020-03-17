package com.sum.test.auto;

import com.sum.test.auto.Friend;
import com.sum.test.auto.Alias;

public class Import {

	public Friend f = new Friend();

	public com.sum.test.auto.Alias a = new com.sum.test.auto.Alias();

	public String testFriend() {
		return f.sayHello();
	}

	public String testAlias() {
		return a.getName();
	}

}
