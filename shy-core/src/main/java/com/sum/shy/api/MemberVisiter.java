package com.sum.shy.api;

import java.util.Map;

import com.sum.pisces.api.Service;
import com.sum.shy.clazz.pojo.IClass;

@Service("memberVisiter")
public interface MemberVisiter {

	public void visitMembers(Map<String, IClass> allClasses);

}
