package com.sum.shy.core.deduce;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IField;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.clazz.api.Member;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Holder;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.processor.MethodResolver;
import com.sum.shy.core.processor.api.Handler;
import com.sum.shy.type.CodeType;
import com.sum.shy.type.api.Type;
import com.sum.shy.visiter.api.Visiter;

public class InvokeVisiter {

	public static Type visitMember(IClass clazz, Member member) {
		// 上锁
		member.lock();
		Type type = member.getType();
		if (type == null) {
			if (member instanceof IField) {
				Stmt stmt = ((IField) member).stmt;
				if (stmt != null && stmt.isAssign()) {
					VariableTracker.trackStmt(clazz, null, null, stmt.line, stmt.subStmt(2, stmt.size()));
					InvokeVisiter.visitStmt(clazz, stmt);
					type = FastDerivator.deriveStmt(clazz, stmt);
				}
			} else if (member instanceof IMethod) {// 如果是方法
				Holder<Type> holder = new Holder<>(new CodeType(clazz, "void"));
				MethodResolver.resolve(clazz, (IMethod) member, new Handler() {
					@Override
					public Object handle(IClass clazz, IMethod method, String indent, String block, Line line,
							Stmt stmt) {
						// 有效返回，才是返回
						if (stmt.isReturn()) {
							Type returnType = FastDerivator.deriveStmt(clazz, stmt.subStmt(1, stmt.size()));
							if (returnType != null)
								holder.obj = returnType;
						}
						return null;
					}
				});
				type = holder.obj;
			}
		}
		// 解锁
		member.unLock();
		return type;
	}

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
		List<Type> paramTypes = token.isInvoke() ? getParamTypes(clazz, token) : null;

		if (token.isType()) {
			token.setTypeAtt(new CodeType(clazz, token));

		} else if (token.isArrayInit() || token.isTypeInit() || token.isCast()) {
			token.setTypeAtt(new CodeType(clazz, token.getTypeNameAtt()));

		} else if (token.isValue()) {
			token.setTypeAtt(FastDerivator.getValueType(clazz, token));

		} else if (token.isSubexpress()) {// 子语句进行推导，以便后续的推导
			Stmt subStmt = token.getSubStmt();
			token.setTypeAtt(FastDerivator.deriveStmt(clazz, subStmt.subStmt(1, subStmt.size() - 1)));

		} else if (token.isInvokeLocal()) {// 本地调用
			Type type = new CodeType(clazz, clazz.typeName);
			Type returnType = visiter.visitMethod(clazz, type, token.getMemberNameAtt(), paramTypes);
			token.setTypeAtt(returnType);

		} else if (token.isVisitField()) {
			Type type = stmt.getToken(index - 1).getTypeAtt();
			Type returnType = visiter.visitField(clazz, type, token.getMemberNameAtt());
			token.setTypeAtt(returnType);

		} else if (token.isInvokeMethod()) {
			Token lastToken = stmt.getToken(index - 1);
			if (lastToken.isOperator() && "?".equals(lastToken.toString()))
				lastToken = stmt.getToken(index - 2);// 如果是判空语句,则向前倒两位 like obj?.do()
			Type type = lastToken.getTypeAtt();
			Type returnType = visiter.visitMethod(clazz, type, token.getMemberNameAtt(), paramTypes);
			token.setTypeAtt(returnType);

		} else if (token.isVisitArrayIndex()) {
			Type type = stmt.getToken(index - 1).getTypeAtt();
			Type returnType = visiter.visitField(clazz, type, token.getMemberNameAtt());
			returnType = visiter.visitMethod(clazz, returnType, "$array_index", null);
			token.setTypeAtt(returnType);

		} else if (token.isArrayIndex()) {
			Type type = token.getTypeAtt();
			Type returnType = visiter.visitMethod(clazz, type, "$array_index", null);
			token.setTypeAtt(returnType);

		}

	}

	public static List<Type> getParamTypes(IClass clazz, Token token) {
		List<Type> parameterTypes = new ArrayList<>();
		Stmt stmt = token.getSubStmt();
		// 只取括号里的
		if (stmt.size() > 3) {// 方法里面必须有参数
			List<Stmt> subStmts = stmt.subStmt(2, stmt.size() - 1).split(",");
			for (Stmt subStmt : subStmts) {
				Type parameterType = FastDerivator.deriveStmt(clazz, subStmt);
				parameterTypes.add(parameterType);
			}
		}
		return parameterTypes;
	}

}
