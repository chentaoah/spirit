package com.sum.shy.core.processor.api;

import com.sum.shy.clazz.IMethod;
import com.sum.shy.core.doc.IClass;
import com.sum.shy.core.doc.Line;
import com.sum.shy.core.doc.Stmt;

public interface Handler {

	Object handle(IClass clazz, IMethod method, String indent, String block, Line line, Stmt stmt);

}
