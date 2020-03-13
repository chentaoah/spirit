package com.sum.test.auto;

import com.sum.test.auto.Friend;
import com.sum.test.auto.Alias;

class Import {

	public Friend f = new Friend();

	public G_Alias a = new G_Alias();

	public String testFriend() {
		return f.sayHello();
	}

	public String testAlias() {
		return a.getName();
	}

}
