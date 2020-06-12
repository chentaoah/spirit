package com.sum.shy.api.service.deducer;

import java.util.ArrayList;
import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.deducer.FastDeducer;
import com.sum.shy.api.deducer.InvokeVisiter;
import com.sum.shy.api.deducer.MemberLinker;
import com.sum.shy.api.deducer.TypeFactory;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IType;
import com.sum.shy.element.Statement;
import com.sum.shy.element.Token;

public class InvokeVisiterImpl implements InvokeVisiter {

	public static FastDeducer deducer = ProxyFactory.get(FastDeducer.class);

	public static MemberLinker linker = ProxyFactory.get(MemberLinker.class);

	public static TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	@Override
	public void visit(IClass clazz, Statement stmt) {
		for (int index = 0; index < stmt.size(); index++) {

			Token token = stmt.getToken(index);

			// 内部可能还需要推导s
			if (token.canSplit())
				visit(clazz, token.getValue());

			if (token.getTypeAtt() != null)
				continue;

			// 参数类型，为了像java那样支持重载
			List<IType> parameterTypes = token.isInvoke() ? getParameterTypes(clazz, token) : null;

			if (token.isType() || token.isArrayInit() || token.isTypeInit() || token.isCast() || token.isValue()) {
				token.setTypeAtt(factory.create(clazz, token));

			} else if (token.isSubexpress()) {// 子语句进行推导，以便后续的推导
				Statement subStmt = token.getValue();
				token.setTypeAtt(deducer.derive(clazz, subStmt.subStmt("(", ")")));

			} else if (token.isLocalMethod()) {// 本地调用
				IType returnType = linker.visitMethod(clazz.toType(), token.getMemberName(), parameterTypes);
				token.setTypeAtt(returnType);

			} else if (token.isVisitField()) {
				IType type = stmt.getToken(index - 1).getTypeAtt();
				IType returnType = linker.visitField(type, token.getMemberName());
				token.setTypeAtt(returnType);

			} else if (token.isInvokeMethod()) {
				IType type = stmt.getToken(index - 1).getTypeAtt();
				IType returnType = linker.visitMethod(type, token.getMemberName(), parameterTypes);
				token.setTypeAtt(returnType);

			} else if (token.isVisitArrayIndex()) {// what like ".str[0]"
				IType type = stmt.getToken(index - 1).getTypeAtt();
				IType returnType = linker.visitField(type, token.getMemberName());
				returnType = factory.create(returnType.getTargetName());
				token.setTypeAtt(returnType);
			}
		}
	}

	public List<IType> getParameterTypes(IClass clazz, Token token) {
		List<IType> parameterTypes = new ArrayList<>();
		Statement stmt = token.getValue();
		if (stmt.size() > 3) {// 方法里面必须有参数
			List<Statement> subStmts = stmt.subStmt(2, stmt.size() - 1).split(",");
			for (Statement subStmt : subStmts) {
				IType parameterType = deducer.derive(clazz, subStmt);
				parameterTypes.add(parameterType);
			}
		}
		return parameterTypes;
	}

}
