package com.sum.shy.core.proc;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.doc.Element;
import com.sum.shy.core.doc.Stmt;
import com.sum.shy.core.doc.Token;
import com.sum.shy.core.type.CodeType;
import com.sum.shy.core.type.api.IType;

public class StmtPreviewer {

//	public static void preview(IClass clazz, Element element) {
//
//		if (element.isDeclare() || element.isDeclareAssign()) {// String text // String text = "abc"
//			Token typeToken = element.stmt.getToken(0);
//			Token varToken = element.stmt.getToken(1);
//			varToken.setTypeAtt(new CodeType(clazz, typeToken));
//
//		} else if (element.isAssign()) {// text = "abc"
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
//
//		} else if (element.isCatch()) {// }catch Exception e{
//			Token typeToken = element.stmt.getToken(2);
//			Token varToken = element.stmt.getToken(3);
//			varToken.setTypeAtt(new CodeType(clazz, typeToken));
//
//		} else if (element.isForIn()) {// for item in list {
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
//
//		} else if (element.isFor()) {// for i=0; i<100; i++ {
//
//		}
//
//	}

}
