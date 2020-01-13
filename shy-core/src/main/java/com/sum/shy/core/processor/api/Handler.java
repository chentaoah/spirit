package com.sum.shy.core.processor.api;

import com.sum.shy.clazz.CtClass;
import com.sum.shy.clazz.CtMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;

public interface Handler {

	Object handle(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt);

}