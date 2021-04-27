package com.sum.spirit.core.api;

import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.clazz.frame.MemberEntity;

public interface ClassVisiter {

	void prepareForVisit(IClass clazz);

	void visitClass(IClass clazz);

	IType visitMember(IClass clazz, MemberEntity member);

}
