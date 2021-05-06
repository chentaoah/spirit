package com.sum.test.type;

import java.util.HashMap;
import com.sum.spirit.test.example.ClassGenericTest;
import com.sum.spirit.test.example.MyTest;
import com.sum.spirit.test.example.GenericType;
import com.sum.test.clazz.ServiceImpl;
import com.sum.test.process.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sum.spirit.lib.Lists;
import java.util.List;
import com.sum.spirit.lib.Maps;
import java.util.Map;

@Deprecated
public class Type {

	public static final Logger logger = LoggerFactory.getLogger(Type.class);
	public static final double NUMBER = 100.0;
	public static final String NAME = "chentao";
	@Deprecated
	public boolean b = true;
	public boolean b1 = false;
	public int i = 100;
	public double d = 100.0;
	public String s = "string";
	public List<Object> emptyList = Lists.of();
	public Map<Object, Object> emptyMap = Maps.of();
	public List<String> list = Lists.of("first", "second", "third");
	public List<Object> list1 = Lists.of("string", 100);
	public List<Map<String, Object>> map = Lists.of(Maps.of("key1", 123, "key2", "123"), Maps.of("key1", 123, "key2", "123"));
	public Map<String, Object> map1 = Maps.of("key1", "string", "key2", 100);
	public boolean[] bArray = new boolean[10];
	public char[] cArray = new char[10];
	public short[] sArray = new short[10];
	public int[] iArray = new int[10];
	public float[] fArray = new float[10];
	public double[] dArray = new double[10];
	public Object[] objArray = new Object[10];
	public String[] strArray = new String[10];
	public String s111 = "I am a new assign syntax!";
	public int num = Main.x;
	public char myChar = 'c';
	public final String cv = "test const voliate";
	public volatile boolean vo = false;

	@Deprecated
	public void testType() {
		boolean a1 = bArray[0];
		char a2 = cArray[0];
		short a3 = sArray[0];
		int a4 = iArray[0];
		float a5 = fArray[0];
		double a6 = dArray[0];
		Object a7 = objArray[0];
		String a8 = strArray[0];
		logger.info("test array{}{}{}{}{}{}{}{}{}", a1, a2, a3, a4, a5, a6, a7, a8);
		Class<?> clazz;
		clazz = Type.class;
		Class<?> clz = clazz;
		logger.info("test class {}", clz);
		Type self = this;
		logger.info("test this {}", self);
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("key", "value");
		long long1 = 100L;
		logger.info("long1 is ", long1);
		long mask = 1L << 8;
		logger.info("mask", mask);
		int[] ints = new int[] { 123, 456, 789 };
		String[] strs = new String[] { "hello", "hello", "hello" };
		logger.info("ints{}", ints);
		logger.info("strs{}", strs[0]);
		int yyy = 1;
		int iii = 100 + yyy++ + 100;
		iii = yyy + 100;
		logger.info(iii + "");
		MyTest objxxx = ClassGenericTest.class.getAnnotation(MyTest.class);
		String ssss = objxxx.value();
		logger.info("" + ssss);
		String xxss = testParam(null, null);
		logger.info(xxss);
		ServiceImpl<String, Object> service = new ServiceImpl<String, Object>();
		String type = service.testReturnGenericType("text");
		logger.info(type.toString());
		String key = service.key;
		logger.info(key);
		int serNum = service.test1(123);
		logger.info("" + serNum);
		String serStr = service.test1("hello");
		logger.info("" + serStr);
		GenericType<String, String> generic = new GenericType<String, String>();
		Integer gKey = generic.get("test");
		logger.info(gKey + "");
		List<Integer> intsss = Lists.of(1, 123, 8987879);
		Integer integer = intsss.get(0);
		int numberxxx = integer.intValue();
		logger.info("" + numberxxx);
		final int finalNum = 1000;
		logger.info("" + finalNum);
		final String strxxxx;
		strxxxx = "hello";
		logger.info(strxxxx);
		boolean bbxxx = getArray()[0];
		logger.info("" + bbxxx);
		Map<String, Object> objvar = Maps.of("name", "chen", "age", 18, "from", "China", "brother", Lists.of("wanhao", "chenzhe"));
		String str0 = objvar.toString();
		logger.info(str0);
	}

	public String testParam(@Deprecated String str, Object obj) {
		return "yes";
	}

	public String testReturnType() {
		int anumx = 1111;
		return "I am a Str!" + anumx;
	}

	public boolean[] getArray() {
		return bArray;
	}

}
