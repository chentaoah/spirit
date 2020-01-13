package com.sum.shy.core.processor.api;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;

public interface Handler {

	Object handle(IClass clazz, IMethod method, String indent, String block, Line line, Stmt stmt);

}
