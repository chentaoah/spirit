package com.sum.shy.java.convert;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.doc.Line;
import com.sum.shy.core.doc.Stmt;
import com.sum.shy.core.doc.Token;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.java.JavaConverter;
import com.sum.shy.java.api.Converter;

public class SimpleConverter implements Converter {

	@Override
	public Stmt convert(IClass clazz, IMethod method, String indent, String block, Line line, Stmt stmt) {

//		if (stmt.isElse() || stmt.isEnd() || stmt.isTry() || stmt.isFinally()) {// } else { // } // try { // } finally {
//			return stmt;
//
//		} else if (stmt.isSuper() || stmt.isThis() || stmt.isFieldAssign() || stmt.isInvoke() || stmt.isReturn()
//				|| stmt.isContinue() || stmt.isBreak() || stmt.isThrow()) {
//			JavaConverter.convert(clazz, stmt);
//			JavaConverter.addLineEnd(clazz, stmt);
//			return stmt;
//
//		} else if (stmt.isDeclare()) {// String str
//			JavaConverter.addLineEnd(clazz, stmt);
//			return stmt;
//
//		} else if (stmt.isCatch()) {// }catch Exception e {
//			JavaConverter.insertBrackets(clazz, stmt);
//			return stmt;
//
//		} else if (stmt.isSync()) {// sync obj {
//			String text = String.format("synchronized (%s) {", stmt.get(1));
//			return new Stmt(text);
//
//		} else if (stmt.isFor()) {// for i=0; i<100; i++ {
//			Token token = stmt.getToken(1);
//			stmt.tokens.add(1, new Token(Constants.TYPE_TOKEN, token.getTypeAtt()));
//			JavaConverter.insertBrackets(clazz, stmt);
//			return stmt;
//
//		} else if (stmt.isForIn()) {// for item in list {
//			Token item = stmt.getToken(1);
//			Token collection = stmt.getToken(3);
//			String text = String.format("for (%s %s : %s) {", item.getTypeAtt(), item, collection);
//			return new Stmt(text);
//
//		} else if (stmt.isAssign()) {// var = list.get(0)
//			Token token = stmt.getToken(0);
//			if (token.isVar() && !token.isDeclaredAtt())
//				stmt.tokens.add(0, new Token(Constants.TYPE_TOKEN, token.getTypeAtt()));
//			JavaConverter.convert(clazz, stmt);
//			JavaConverter.addLineEnd(clazz, stmt);
//			return stmt;
//
//		} else if (stmt.isJudgeInvoke()) {// obj?.invoke()
//			String var = stmt.get(0);
//			String invoke = stmt.get(2);
//			String text = String.format("if (%s != null)\n%s%s%s;", var, indent + "\t", var, invoke);
//			return new Stmt(text);
//
//		}

//		System.out.println(stmt);
//		System.out.println(stmt.syntax);
		throw new RuntimeException("The syntax statement is not supported!");

	}

}
