package com.sum.shy.core.proc;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.doc.Element;
import com.sum.shy.core.doc.Stmt;
import com.sum.shy.core.doc.Token;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.type.CodeType;
import com.sum.shy.core.type.api.IType;
import com.sum.shy.core.visiter.api.Visiter;

public class InvokeVisiter {

	public static void visitStmt(IClass clazz, Stmt stmt) {
		for (int i = 0; i < stmt.size(); i++)
			visitToken(clazz, stmt, i, stmt.getToken(i));
	}

	public static void visitToken(IClass clazz, Stmt stmt, int index, Token token) {

		// 获取推导器
		Visiter visiter = Context.get().visiter;

		// 内部可能还需要推导
		if (token.hasSubStmt())
			visitStmt(clazz, token.getSubStmt());
		if (token.isNode())
			visitStmt(clazz, token.getNode().toStmt());

		// 参数类型，为了像java那样支持重载
		List<IType> paramTypes = token.isInvoke() ? getParamTypes(clazz, token) : null;

		if (token.isType()) {
			token.setTypeAtt(new CodeType(clazz, token));

		} else if (token.isArrayInit() || token.isTypeInit() || token.isCast()) {
			token.setTypeAtt(new CodeType(clazz, token.getTypeNameAtt()));

		} else if (token.isValue()) {
			token.setTypeAtt(TypeDeducer.getValueType(clazz, token));

		} else if (token.isSubexpress()) {// 子语句进行推导，以便后续的推导
			Stmt subStmt = token.getSubStmt();
			token.setTypeAtt(TypeDeducer.deriveStmt(clazz, subStmt.subStmt(1, subStmt.size() - 1)));

		} else if (token.isLocalMethod()) {// 本地调用
			IType type = new CodeType(clazz, clazz.getTypeName());
			IType returnType = visiter.visitMethod(clazz, type, token.getMemberNameAtt(), paramTypes);
			token.setTypeAtt(returnType);

		} else if (token.isVisitField()) {
			IType type = stmt.getToken(index - 1).getTypeAtt();
			IType returnType = visiter.visitField(clazz, type, token.getMemberNameAtt());
			token.setTypeAtt(returnType);

		} else if (token.isInvokeMethod()) {
			Token lastToken = stmt.getToken(index - 1);
			if (lastToken.isOperator() && "?".equals(lastToken.toString()))
				lastToken = stmt.getToken(index - 2);// 如果是判空语句,则向前倒两位 like obj?.do()
			IType type = lastToken.getTypeAtt();
			IType returnType = visiter.visitMethod(clazz, type, token.getMemberNameAtt(), paramTypes);
			token.setTypeAtt(returnType);

		} else if (token.isVisitArrayIndex()) {
			IType type = stmt.getToken(index - 1).getTypeAtt();
			IType returnType = visiter.visitField(clazz, type, token.getMemberNameAtt());
			returnType = visiter.visitMethod(clazz, returnType, Constants.$ARRAY_INDEX, null);
			token.setTypeAtt(returnType);

		} else if (token.isArrayIndex()) {
			IType type = token.getTypeAtt();
			IType returnType = visiter.visitMethod(clazz, type, Constants.$ARRAY_INDEX, null);
			token.setTypeAtt(returnType);

		}

	}

	public static List<IType> getParamTypes(IClass clazz, Token token) {
		List<IType> paramTypes = new ArrayList<>();
		Stmt stmt = token.getSubStmt();
		// 只取括号里的
		if (stmt.size() > 3) {// 方法里面必须有参数
			List<Stmt> subStmts = stmt.subStmt(2, stmt.size() - 1).split(",");
			for (Stmt subStmt : subStmts) {
				IType parameterType = TypeDeducer.deriveStmt(clazz, subStmt);
				paramTypes.add(parameterType);
			}
		}
		return paramTypes;
	}

}
