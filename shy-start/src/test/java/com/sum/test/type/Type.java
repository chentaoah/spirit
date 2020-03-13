package com.sum.test.type;

import java.util.HashMap;
import com.sum.shy.lib.Collection;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
class Type {

	public boolean b = true;

	public boolean b1 = false;

	public int i = 100;

	public double d = 100.0;

	public String s = "string";

	public List<String> list = Collection.newArrayList( "first", "second", "third" );

	public List<Object> list1 = Collection.newArrayList( "string", 100 );

	public Map<String, Integer> map = Collection.newHashMap( "key1", 123, "key2", 456 );

	public Map<String, Object> map1 = Collection.newHashMap( "key1", "string", "key2", 100 );

	public boolean[] bArray = new boolean[10];

	public char[] cArray = new char[10];

	public short[] sArray = new short[10];

	public int[] iArray = new int[10];

	public float[] fArray = new float[10];

	public double[] dArray = new double[10];

	public Object[] objArray = new Object[10];

	public String[] strArray = new String[10];

	public String s111 = "I am a new assign syntax!";

	public void testType() {
		boolean a1 = bArray[0];
		char a2 = cArray[0];
		short a3 = sArray[0];
		int a4 = iArray[0];
		float a5 = fArray[0];
		double a6 = dArray[0];
		Object a7 = objArray[0];
		String a8 = strArray[0];
		logger.info( "test array{}{}{}{}{}{}{}{}{}", a1, a2, a3, a4, a5, a6, a7, a8 );;
		Class<?> clazz;
		clazz = Type.class;
		Class<?> clz = clazz;
		logger.info( "test class {}", clz );;
		Type self = this;
		logger.info( "test this {}", self );;
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("key", "value");
		long long1 = 100L;
		logger.info( "long1 is ", long1 );;
		long mask = 1L << 8;
		logger.info( "mask", mask );;
	}

	public String testParam(@Deprecated String str) {
		return "yes";
	}

}
