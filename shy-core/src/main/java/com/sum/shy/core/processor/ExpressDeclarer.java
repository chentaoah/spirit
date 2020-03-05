package com.sum.shy.core.processor;

import java.util.Map;

import com.sum.shy.core.ElementVisiter;
import com.sum.shy.core.MemberVisiter.MethodContext;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.Variable;
import com.sum.shy.core.document.Element;
import com.sum.shy.core.document.Line;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.type.api.IType;

public class ExpressDeclarer {

	public static void declare(IClass clazz, MethodContext context, Element element) {

		if (element.isAssign()) {// text = "abc"
			Stmt stmt = element.stmt;
			Stmt subStmt = stmt.subStmt(2, stmt.size());
			VariableTracker.trackStmt(clazz, context, subStmt);
			InvokeVisiter.visitStmt(clazz, subStmt);
			IType type = FastDeducer.deriveStmt(clazz, subStmt);
			Token varToken = element.stmt.getToken(0);
			varToken.setTypeAtt(type);

		} else if (element.isForIn()) {// for item in list {
			Stmt stmt = element.stmt;
			Stmt subStmt = stmt.subStmt(3, stmt.size() - 1);
			VariableTracker.trackStmt(clazz, context, subStmt);
			InvokeVisiter.visitStmt(clazz, subStmt);
			IType type = FastDeducer.deriveStmt(clazz, subStmt);
			Token varToken = element.stmt.getToken(1);
			varToken.setTypeAtt(type);

		} else if (element.isFor()) {// for i=0; i<100; i++ {
			Stmt stmt = element.stmt;
			Stmt subStmt = stmt.subStmt(1, stmt.indexOf(";"));
			Element subElement = new Element(new Line(subStmt.toString()));
			subElement.stmt = subStmt;// 替换一下
			Map<String, Object> result = ElementVisiter.visit(clazz, context, subElement);
			if (result != null) {
				Variable variable = new Variable();
				variable.type = (IType) result.get(FastDeducer.TYPE);
				variable.name = (String) result.get(FastDeducer.NAME);
				variable.blockId = context.getBlockId();
				context.variables.add(variable);
			}
		}

	}

}
