package com.sum.shy.core.processor;

import com.sum.shy.core.ElementVisiter;
import com.sum.shy.core.MemberVisiter.MethodContext;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.Variable;
import com.sum.shy.core.document.Element;
import com.sum.shy.core.document.Line;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.type.CodeType;
import com.sum.shy.core.type.api.IType;

public class ExpressDeclarer {

	public static void declare(IClass clazz, MethodContext context, Element element) {

		if (element.isAssign()) {// text = "abc"
			Stmt subStmt = element.subStmt(2, element.getSize());
			VariableTracker.trackStmt(clazz, context, subStmt);
			InvokeVisiter.visitStmt(clazz, subStmt);
			IType type = FastDeducer.deriveStmt(clazz, subStmt);
			Token varToken = element.getToken(0);
			Variable variable = new Variable(context.getBlockId(), type, varToken.toString());
			context.variables.add(variable);

		} else if (element.isForIn()) {// for item in list {
			Stmt subStmt = element.subStmt(3, element.getSize() - 1);
			VariableTracker.trackStmt(clazz, context, subStmt);
			InvokeVisiter.visitStmt(clazz, subStmt);
			IType type = FastDeducer.deriveStmt(clazz, subStmt);
			// 这里从数组或集合中获取类型
			type = type.isArray() ? new CodeType(clazz, type.getTypeName()) : type.getGenericTypes().get(0);
			Token varToken = element.getToken(1);
			Variable variable = new Variable(context.getBlockId(), type, varToken.toString());
			context.variables.add(variable);

		} else if (element.isFor()) {// for i=0; i<100; i++ {
			Stmt subStmt = element.subStmt(1, element.indexOf(";"));
			Element subElement = new Element(new Line(subStmt.toString()));
			subElement.stmt = subStmt;// 替换一下
			Variable variable = ElementVisiter.visit(clazz, context, subElement);
			if (variable != null)
				context.variables.add(variable);

		}

	}

}
