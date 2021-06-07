package com.gitee.spirit.core.api;

import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.clazz.frame.MemberEntity;

public interface ClassVisiter {

	void prepareForVisit(IClass clazz);

	void visitClass(IClass clazz);

	IType visitMember(IClass clazz, MemberEntity member);

}
