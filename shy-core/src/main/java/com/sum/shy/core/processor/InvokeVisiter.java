package com.sum.shy.core.processor;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.clazz.type.TypeFactory;
import com.sum.shy.core.clazz.type.TypeLinker;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.entity.Constants;

public class InvokeVisiter {

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

		if (token.getTypeAtt() == null) {

			if (token.isType() || token.isArrayInit() || token.isTypeInit() || token.isCast() || token.isValue()) {
				token.setTypeAtt(TypeFactory.resolve(clazz, token));

			} else if (token.isSubexpress()) {// 子语句进行推导，以便后续的推导
				token.setTypeAtt(FastDeducer.deriveStmt(clazz, token.getStmt().subStmt("(", ")")));

			} else if (token.isLocalMethod()) {// 本地调用
				IType type = TypeFactory.create(clazz, clazz.getTypeName());
				IType returnType = TypeLinker.visitMethod(type, token.getMemberNameAtt(), parameterTypes);
				token.setTypeAtt(returnType);

			} else if (token.isVisitField()) {
				IType type = stmt.getToken(index - 1).getTypeAtt();
				IType returnType = null;
				if (type.isArray() && Constants.ARRAY_LENGTH.equals(token.getMemberNameAtt())) {// 访问数组length直接返回int类型
					returnType = TypeFactory.create(clazz, Constants.INT);
				} else {
					returnType = TypeLinker.visitField(type, token.getMemberNameAtt());
				}
				token.setTypeAtt(returnType);

			} else if (token.isInvokeMethod()) {
				IType type = stmt.getToken(index - 1).getTypeAtt();
				IType returnType = TypeLinker.visitMethod(type, token.getMemberNameAtt(), parameterTypes);
				token.setTypeAtt(returnType);

			} else if (token.isVisitArrayIndex()) {
				IType type = stmt.getToken(index - 1).getTypeAtt();
				IType returnType = TypeLinker.visitField(type, token.getMemberNameAtt());
				returnType = TypeFactory.create(clazz, returnType.getTypeName());
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
