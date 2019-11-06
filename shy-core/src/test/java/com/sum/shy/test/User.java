package com.sum.shy.test;

import java.util.List;
import java.util.Map;
import com.sum.shy.library.Collection;
import com.sum.shy.library.StringUtils;
import com.sum.shy.core.People;

public class User {
	public static int a = 101;
	public int b = 11;
	public int c = a;
	public Map<String,Integer> d = Collection.newHashMap("chentao",1,"zjx",2);
	public Map<String,Integer> e = d;
	public double f = 99.0;
	public String g = "hello world";
	public People i = People.getInstance();
	public String zzzzzzz = "hello";
	public List<String> j = Collection.newArrayList("a",zzzzzzz,"c","d{}[]");
	public List<Object> k = Collection.newArrayList();
	public boolean m = false;

	public static void main(){
	}

	public void getName(int x,int y){
		User user = null;
		if (a > 0 && y < 10){
			user = new User();
		}
	}

	public int getAge(int bbb,int y){
		int zzz = bbb;
		return zzz;
	}

	public List<Object> getResult(int x,int y){
		User user = null;
		if (x > 0 && b < 0){
			List<String> list = Collection.newArrayList("a","b","c","d{}[]");
		}else if (x < 0 && y > 0){
		}
		String z = "hello";
		if (!StringUtils.isNotEmpty(z) || StringUtils.equals(z,"hell")){
		}
		List<Object> list = Collection.newArrayList();
		User zzz = (User)list.get(212);
		zzz.getName(1,2);
		Map<Object,Object> map = Collection.newHashMap();
		User qqq = (User)map.get("chentao");
		List<Object> list2 = list;
		return list2;
	}

}
