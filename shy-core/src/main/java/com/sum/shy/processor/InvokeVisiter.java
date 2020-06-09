package com.sum.shy.processor;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IType;
import com.sum.shy.deducer.AdaptiveLinker;
import com.sum.shy.deducer.TypeFactory;
import com.sum.shy.element.Stmt;
import com.sum.shy.element.Token;

public class InvokeVisiter {

	public static void visitStmt(IClass clazz, Stmt stmt) {
		for (int index = 0; index < stmt.size(); index++) {
			Token token = stmt.getToken(index);
			// 内部可能还需要推导s
			if (token.canVisit())
				visitStmt(clazz, token.getStmt());

			if (token.getTypeAtt() != null)
				continue;

			// 参数类型，为了像java那样支持重载
			List<IType> parameterTypes = token.isInvoke() ? getParameterTypes(clazz, token) : null;

			if (token.isType() || token.isArrayInit() || token.isTypeInit() || token.isCast() || token.isValue()) {
				token.setTypeAtt(TypeFactory.create(clazz, token));

			} else if (token.isSubexpress()) {// 子语句进行推导，以便后续的推导
				token.setTypeAtt(FastDeducer.deriveStmt(clazz, token.getStmt().subStmt("(", ")")));

			} else if (token.isLocalMethod()) {// 本地调用
				IType returnType = AdaptiveLinker.visitMethod(clazz.toType(), token.getMemberNameAtt(), parameterTypes);
				token.setTypeAtt(returnType);

			} else if (token.isVisitField()) {
				IType type = stmt.getToken(index - 1).getTypeAtt();
				IType returnType = AdaptiveLinker.visitField(type, token.getMemberNameAtt());
				token.setTypeAtt(returnType);

			} else if (token.isInvokeMethod()) {
				IType type = stmt.getToken(index - 1).getTypeAtt();
				IType returnType = AdaptiveLinker.visitMethod(type, token.getMemberNameAtt(), parameterTypes);
				token.setTypeAtt(returnType);

			} else if (token.isVisitArrayIndex()) {// what like ".str[0]"
				IType type = stmt.getToken(index - 1).getTypeAtt();
				IType returnType = AdaptiveLinker.visitField(type, token.getMemberNameAtt());
				returnType = TypeFactory.create(returnType.getTargetName());
				token.setTypeAtt(returnType);
			}
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
