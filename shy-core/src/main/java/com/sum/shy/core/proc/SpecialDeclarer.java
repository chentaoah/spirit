package com.sum.shy.core.proc;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.doc.Element;
import com.sum.shy.core.doc.Stmt;
import com.sum.shy.core.doc.Token;
import com.sum.shy.core.type.api.IType;

public class SpecialDeclarer {

	public static void declare(IClass clazz, Element element) {

		if (element.isAssign()) {// text = "abc"
			Stmt stmt = element.stmt;
			Stmt subStmt = stmt.subStmt(2, stmt.size());
			VariableTracker.trackStmt(clazz, subStmt);
			InvokeVisiter.visitStmt(clazz, subStmt);
			IType type = FastDeducer.deriveStmt(clazz, subStmt);
			Token varToken = element.stmt.getToken(0);
			varToken.setTypeAtt(type);

		} else if (element.isForIn()) {// for item in list {
			Stmt stmt = element.stmt;
			Stmt subStmt = stmt.subStmt(3, stmt.size() - 1);
			VariableTracker.trackStmt(clazz, subStmt);
			InvokeVisiter.visitStmt(clazz, subStmt);
			IType type = FastDeducer.deriveStmt(clazz, subStmt);
			Token varToken = element.stmt.getToken(1);
			varToken.setTypeAtt(type);

		} else if (element.isFor()) {// for i=0; i<100; i++ {

		}

	}

}
