package com.sum.shy.api;

import java.util.Map;

import com.sum.pisces.api.Service;
import com.sum.shy.clazz.IClass;

@Service("member_visiter")
public interface MemberVisiter {

	public void visitMembers(Map<String, IClass> allClasses);

}
