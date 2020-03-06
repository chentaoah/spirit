package com.sum.shy.core.processor;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.type.CodeType;
import com.sum.shy.core.type.api.IType;
import com.sum.shy.core.visiter.AdaptiveVisiter;
import com.sum.shy.core.visiter.api.Visiter;

public class InvokeVisiter {

	// 推导器
	public static Visiter visiter = new AdaptiveVisiter();

	public static void visitStmt(IClass clazz, Stmt stmt) {
		for (int i = 0; i < stmt.size(); i++)
			visitToken(clazz, stmt, i, stmt.getToken(i));
	}

	public static void visitToken(IClass clazz, Stmt stmt, int index, Token token) {

		// 内部可能还需要推导
		if (token.hasStmt())
			visitStmt(clazz, token.getStmt());

		// 参数类型，为了像java那样支持重载
		List<IType> parameterTypes = token.isInvoke() ? getParameterTypes(clazz, token) : null;

		if (token.isType()) {
			token.setTypeAtt(new CodeType(clazz, token));

		} else if (token.isArrayInit() || token.isTypeInit() || token.isCast()) {
			token.setTypeAtt(new CodeType(clazz, token.getTypeNameAtt()));

		} else if (token.isValue()) {
			token.setTypeAtt(FastDeducer.getValueType(clazz, token));

		} else if (token.isSubexpress()) {// 子语句进行推导，以便后续的推导
			Stmt subStmt = token.getStmt();
			token.setTypeAtt(FastDeducer.deriveStmt(clazz, subStmt.subStmt(1, subStmt.size() - 1)));

		} else if (token.isLocalMethod()) {// 本地调用
			IType type = new CodeType(clazz, clazz.getTypeName());
			IType returnType = visiter.visitMethod(clazz, type, token.getMemberNameAtt(), parameterTypes);
			token.setTypeAtt(returnType);

		} else if (token.isVisitField()) {
			IType type = stmt.getToken(index - 1).getTypeAtt();
			IType returnType = visiter.visitField(clazz, type, token.getMemberNameAtt());
			token.setTypeAtt(returnType);

		} else if (token.isInvokeMethod()) {
			IType type = stmt.getToken(index - 1).getTypeAtt();
			IType returnType = visiter.visitMethod(clazz, type, token.getMemberNameAtt(), parameterTypes);
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

	public static List<IType> getParameterTypes(IClass clazz, Token token) {
		List<IType> parameterTypes = new ArrayList<>();
		Stmt stmt = token.getStmt();
		if (stmt.size() > 3) {// 方法里面必须有参数
			List<Stmt> subStmts = stmt.subStmt(2, stmt.size() - 1).split(",");
			for (Stmt subStmt : subStmts) {
				IType parameterType = FastDeducer.deriveStmt(clazz, subStmt);
				parameterTypes.add(parameterType);
			}
		}
		return parameterTypes;
	}

}
