package com.sum.test.type;

import com.sum.shy.library.Collection;
import java.util.List;
import java.util.Map;

public class Type {

	public boolean b = true;
	public boolean b1 = false;
	public int i = 100;
	public double d = 100.0;
	public String s = "string";
	public List<String> list = Collection.newArrayList("first", "second", "third");
	public List<Object> list1 = Collection.newArrayList("string", 100);
	public Map<String, Integer> map = Collection.newHashMap("key1", 123, "key2", 456);
	public Map<String, Object> map1 = Collection.newHashMap("key1", "string", "key2", 100);
	public boolean[] bArray = new boolean[10];
	public char[] cArray = new char[10];
	public short[] sArray = new short[10];
	public int[] iArray = new int[10];
	public float[] fArray = new float[10];
	public double[] dArray = new double[10];
	public Object[] objArray = new Object[10];
	public String[] strArray = new String[10];

	public void testType() {
		list.add("fourth");
		list1.add(100.0);
		map.put("key3", 100);
		map1.put("key3", 100.0);
	}

}
