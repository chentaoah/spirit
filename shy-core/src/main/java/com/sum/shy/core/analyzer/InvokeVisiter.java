package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.api.Handler;
import com.sum.shy.core.clazz.api.Member;
import com.sum.shy.core.clazz.impl.CtClass;
import com.sum.shy.core.clazz.impl.CtField;
import com.sum.shy.core.clazz.impl.CtMethod;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Holder;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.type.api.Type;
import com.sum.shy.core.type.impl.CodeType;
import com.sum.shy.lib.StringUtils;

public class InvokeVisiter {

	public static Type visitMember(CtClass clazz, Member member) {
		// 上锁
		member.lock();
		Type type = member.getType();
		if (type == null) {
			if (member instanceof CtField) {
				Stmt stmt = ((CtField) member).stmt;
				if (stmt.isAssign()) {
					VariableTracker.track(clazz, null, null, stmt.line, stmt.subStmt(2, stmt.size()));
					InvokeVisiter.visitStmt(clazz, stmt);
					type = FastDerivator.deriveStmt(clazz, stmt);
				}

			} else if (member instanceof CtMethod) {// 如果是方法
				Holder<Type> holder = new Holder<>(new CodeType(clazz, "void"));
				MethodResolver.resolve(clazz, (CtMethod) member, new Handler() {
					@Override
					public Object handle(CtClass clazz, CtMethod method, String indent, String block, Line line,
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

	public static void visitStmt(CtClass clazz, Stmt stmt) {
		for (int i = 0; i < stmt.size(); i++) {
			visitToken(clazz, stmt, i, stmt.getToken(i));
		}
	}

	public static void visitToken(CtClass clazz, Stmt stmt, int index, Token token) {
		// 内部可能还需要推导
		if (token.hasSubStmt())
			visitStmt(clazz, (Stmt) token.value);

		// 参数类型，为了像java那样支持重载
		List<Type> parameterTypes = token.isInvoke() ? getParameterTypes(clazz, token) : null;

		if (token.isType()) {
			token.setTypeAtt(new CodeType(clazz, token));

		} else if (token.isArrayInit() || token.isTypeInit() || token.isCast()) {
			token.setTypeAtt(new CodeType(clazz, token.getTypeNameAtt()));

		} else if (token.isValue()) {
			token.setTypeAtt(FastDerivator.getValueType(clazz, token));

		} else if (token.isSubexpress()) {// 子语句进行推导，以便后续的推导
			Stmt subStmt = (Stmt) token.value;
			token.setTypeAtt(FastDerivator.deriveStmt(clazz, subStmt.subStmt(1, subStmt.size() - 1)));

		} else if (token.isInvokeLocal()) {// 本地调用
			Type type = new CodeType(clazz, clazz.typeName);
			Type returnType = visitMethod(clazz, type, token.getMemberNameAtt(), parameterTypes);
			token.setTypeAtt(returnType);

		} else if (token.isVisitField()) {
			Type type = stmt.getToken(index - 1).getTypeAtt();
			Type returnType = visitField(clazz, type, token.getMemberNameAtt());
			token.setTypeAtt(returnType);

		} else if (token.isInvokeMethod()) {
			Token lastToken = stmt.getToken(index - 1);
			if (lastToken.isOperator() && "?".equals(lastToken.value))
				lastToken = stmt.getToken(index - 2);// 如果是判空语句,则向前倒两位 like obj?.do()
			Type type = lastToken.getTypeAtt();
			Type returnType = visitMethod(clazz, type, token.getMemberNameAtt(), parameterTypes);
			token.setTypeAtt(returnType);

		} else if (token.isVisitArrayIndex()) {
			Type type = stmt.getToken(index - 1).getTypeAtt();
			Type returnType = visitField(clazz, type, token.getMemberNameAtt());
			returnType = visitMethod(clazz, returnType, "$array_index", null);
			token.setTypeAtt(returnType);

		} else if (token.isArrayIndex()) {
			Type type = token.getTypeAtt();
			Type returnType = visitMethod(clazz, type, "$array_index", null);
			token.setTypeAtt(returnType);
		}

	}

	private static List<Type> getParameterTypes(CtClass clazz, Token token) {
		List<Type> parameterTypes = new ArrayList<>();
		Stmt stmt = (Stmt) token.value;
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

	public static Type visitField(CtClass clazz, Type type, String fieldName) {
		if (type.isArray()) {
			if ("length".equals(fieldName))
				return new CodeType(clazz, "int");
			throw new RuntimeException("Some functions of array are not supported yet!");
		} else {
			String className = type.getClassName();
			if (Context.get().contains(className)) {
				CtClass typeClass = Context.get().findClass(className);
				if (StringUtils.isNotEmpty(fieldName)) {
					if ("class".equals(fieldName)) {
						return new CodeType(typeClass, "Class<?>");
					}
					if (typeClass.existField(fieldName)) {
						CtField field = typeClass.findField(fieldName);
						return visitMember(typeClass, field);

					} else if (StringUtils.isNotEmpty(typeClass.superName)) {
						return visitField(typeClass, new CodeType(typeClass, typeClass.superName), fieldName);
					}
				}
			} else {
				return NativeLinker.visitField(clazz, type, fieldName);
			}
		}
		return null;
	}

	public static Type visitMethod(CtClass clazz, Type type, String methodName, List<Type> parameterTypes) {
		if (type.isArray()) {
			if ("$array_index".equals(methodName))
				return new CodeType(clazz, type.getTypeName());
			throw new RuntimeException("Some functions of array are not supported yet!");
		} else {
			String className = type.getClassName();
			if (Context.get().contains(className)) {
				CtClass typeClass = Context.get().findClass(className);
				if (StringUtils.isNotEmpty(methodName)) {
					if ("super".equals(methodName)) {
						return new CodeType(typeClass, typeClass.superName);
					}
					if (typeClass.existMethod(methodName, parameterTypes)) {
						CtMethod method = typeClass.findMethod(methodName, parameterTypes);
						return visitMember(typeClass, method);

					} else if (StringUtils.isNotEmpty(typeClass.superName)) {
						return visitMethod(typeClass, new CodeType(typeClass, typeClass.superName), methodName,
								parameterTypes);
					}
				}
			} else {
				return NativeLinker.visitMethod(clazz, type, methodName, parameterTypes);
			}
		}
		return null;
	}

}
