package com.sum.shy.api;

import java.util.Map;

import com.sum.pisces.api.Service;
import com.sum.shy.clazz.AbsMember;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IType;

@Service("member_visiter")
public interface MemberVisiter {

	void visit(Map<String, IClass> allClasses);

	IType visitMember(IClass clazz, AbsMember member);

}
