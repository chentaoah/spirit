package com.sum.shy.core.proc;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.doc.Element;
import com.sum.shy.core.doc.Stmt;
import com.sum.shy.core.doc.Token;
import com.sum.shy.core.type.CodeType;

public class StmtPreviewer {

	public static void preview(IClass clazz, Element element) {

		// String text
		// String text = "abc"
		// }catch Exception e{
		assignType(clazz, element.stmt);// 类型赋值

		// text = "abc"
		// for item in list {
		// for i=0; i<100; i++ {

		if (element.isAssign()) {
//			Stmt stmt = element.stmt;
//			Stmt subStmt = stmt.subStmt(2, stmt.size());
//			// 变量追踪
//			VariableTracker.trackStmt(clazz, subStmt);
//			// 调用推导
//			InvokeVisiter.visitStmt(clazz, subStmt);
//			// 类型推导
//			IType type = TypeDeducer.deriveStmt(clazz, subStmt);
//			// 设置类型
//			Token varToken = element.stmt.getToken(0);
//			varToken.setTypeAtt(type);

		} else if (element.isForIn()) {
//			Stmt stmt = element.stmt;
//			Stmt subStmt = stmt.subStmt(3, stmt.size() - 1);
//			// 变量追踪
//			VariableTracker.trackStmt(clazz, subStmt);
//			// 调用推导
//			InvokeVisiter.visitStmt(clazz, subStmt);
//			// 类型推导
//			IType type = TypeDeducer.deriveStmt(clazz, subStmt);
//			// 设置类型
//			Token varToken = element.stmt.getToken(1);
//			varToken.setTypeAtt(type);

		} else if (element.isFor()) {

		}

	}

	public static void assignType(IClass clazz, Stmt stmt) {
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.hasSubStmt()) {
				assignType(clazz, token.getSubStmt());

			} else if (token.isType()) {
				if (i + 1 < stmt.size()) {
					Token nextToken = stmt.getToken(i + 1);
					if (nextToken.isVar()) {
						nextToken.setTypeAtt(new CodeType(clazz, token));
					}
				}
			}
		}
	}

}
