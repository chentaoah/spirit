package com.sum.shy.core.api;

import com.sum.shy.core.clazz.impl.CtClass;
import com.sum.shy.core.clazz.impl.CtMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;

public interface Handler {

	Object handle(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt);

}
