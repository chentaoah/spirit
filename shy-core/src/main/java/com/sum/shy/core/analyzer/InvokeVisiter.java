package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.sum.shy.lib.Collection;
import com.sum.shy.lib.StringUtils;

public class InvokeVisiter {

	/**
	 * 推导类
	 * 
	 * @param clazz
	 */
	public static void visitClass(CtClass clazz) {
		for (Member element : clazz.getAllElement()) {
			element.setType(visitElement(clazz, element));
		}
	}

	/**
	 * 如果字段类型还没有推导出来，则进行深度推导
	 * 
	 * @param clazz
	 * @param element
	 * @return
	 */
	public static Type visitElement(CtClass clazz, Member element) {
		// 上锁
		element.lock();
		Type type = element.getType();
		if (type == null) {

			if (element instanceof CtField) {// 如果是字段
				Stmt stmt = ((CtField) element).stmt;
				VariableTracker.track(clazz, null, null, stmt.line, stmt);// 变量追踪一下
				visitStmt(clazz, stmt);// 推导类型
				type = FastDerivator.getType(clazz, stmt);// 快速推导

			} else if (element instanceof CtMethod) {// 如果是方法
				Holder<Type> holder = new Holder<>(new CodeType(clazz, "void"));
				MethodResolver.resolve(clazz, (CtMethod) element, new Handler() {
					@Override
					public Object handle(CtClass clazz, CtMethod method, String indent, String block, Line line,
							Stmt stmt) {
						// 有效返回，才是返回
						if (stmt.isReturn()) {
							Type returnType = FastDerivator.getType(clazz, stmt.subStmt(1, stmt.size()));
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
		element.unLock();
		return type;
	}

	public static void visitStmt(CtClass clazz, Stmt stmt) {
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			visitToken(clazz, stmt, i, token);
		}
	}

	public static void visitToken(CtClass clazz, Stmt stmt, int index, Token token) {
		// 内部可能还需要推导
		if (token.hasSubStmt()) {
			visitStmt(clazz, (Stmt) token.value);
		}

		// 参数类型，为了像java那样支持重载
		List<Type> parameterTypes = getParameterTypes(clazz, token);

		if (token.isSubexpress()) {// 子语句进行推导，以便后续的推导
			Stmt subStmt = (Stmt) token.value;
			token.setTypeAtt(FastDerivator.getType(clazz, subStmt.subStmt(1, subStmt.size() - 1)));

		} else if (token.isTypeInit()) {
			token.setTypeAtt(new CodeType(clazz, token.getTypeNameAtt()));

		} else if (token.isInvokeLocal()) {// 本地调用
			Type type = new CodeType(clazz, clazz.typeName);
			Type returnType = getReturnType(clazz, type, null, token.getMemberNameAtt(), parameterTypes);
			token.setTypeAtt(returnType);

		} else if (token.isVisitField()) {
			Type type = stmt.getToken(index - 1).getTypeAtt();
			Type returnType = getReturnType(clazz, type, Arrays.asList(token.getMemberNameAtt()), null, null);
			token.setTypeAtt(returnType);

		} else if (token.isInvokeMethod()) {
			// 如果是判空语句,则向前倒两位 like obj?.do()
			Token lastToken = stmt.getToken(index - 1);
			if (lastToken.isOperator() && "?".equals(lastToken.value))
				lastToken = stmt.getToken(index - 2);
			// ?号前面可能是变量也可能是方法调用
			Type type = lastToken.getTypeAtt();
			Type returnType = getReturnType(clazz, type, null, token.getMemberNameAtt(), parameterTypes);
			token.setTypeAtt(returnType);

		} else if (token.isQuickIndex()) {
			Type type = token.getTypeAtt();
			Type returnType = getReturnType(clazz, type, Arrays.asList(token.getMemberNameAtt()), "$quick_index", null);
			token.setTypeAtt(returnType);

		}

	}

	private static List<Type> getParameterTypes(CtClass clazz, Token token) {
		List<Type> parameterTypes = new ArrayList<>();
		if (token.isInvoke()) {
			Stmt stmt = (Stmt) token.value;
			// 只取括号里的
			if (stmt.size() > 3) {// 方法里面必须有参数
				List<Stmt> subStmts = stmt.subStmt(2, stmt.size() - 1).split(",");
				for (Stmt subStmt : subStmts) {
					Type parameterType = FastDerivator.getType(clazz, subStmt);
					parameterTypes.add(parameterType);
				}
			}
		}
		return parameterTypes;
	}

	public static Type getReturnType(CtClass clazz, Type type, List<String> members, String methodName,
			List<Type> parameterTypes) {

		if (type.isArray()) {// 如果是一个数组，只支持调用length
			if (members != null && members.size() == 1) {
				String member = members.remove(0);// 获取第一个属性
				if ("length".equals(member)) {
					return new CodeType(clazz, "int");
				}
			} else if ("$quick_index".equals(methodName)) {
				return new CodeType(clazz, type.getTypeName());
			}
			throw new RuntimeException("Some functions of array are not supported yet!");

		} else {

			String className = type.getClassName();// 类名
			if (Context.get().contains(className)) {// 看下上下文中是否包含
				CtClass typeClass = Context.get().findClass(className);// 获取友元
				if (members != null && members.size() > 0) {

					String member = members.remove(0);// 获取第一个属性
					// 特殊属性class
					if ("class".equals(member)) {
						return new CodeType(typeClass, "Class<?>");
					}
					// 存在该字段
					if (typeClass.existField(member)) {
						CtField field = typeClass.findField(member);
						Type returnType = visitElement(typeClass, field);// 可能字段类型还需要进行深度推导
						if (members.size() > 0 || methodName != null)
							returnType = getReturnType(typeClass, returnType, members, methodName, parameterTypes);
						return returnType;

					} else if (StringUtils.isNotEmpty(typeClass.superName)) {// 如果不存在该属性，则向上寻找
						// 父类可能是java里面的类
						Type returnType = getReturnType(typeClass, new CodeType(typeClass, typeClass.superName),
								Collection.newArrayList(member), null, null);
						if (members.size() > 0 || methodName != null)
							returnType = getReturnType(typeClass, returnType, members, methodName, parameterTypes);
						return returnType;

					}

				} else if (methodName != null) {
					// 父类构造方法
					if ("super".equals(methodName)) {
						return new CodeType(typeClass, typeClass.superName);
					}
					// 存在该方法
					if (typeClass.existMethod(methodName, parameterTypes)) {
						CtMethod method = typeClass.findMethod(methodName, parameterTypes);
						return visitElement(typeClass, method);// 可能字段类型还需要进行深度推导

					} else if (StringUtils.isNotEmpty(typeClass.superName)) {
						return getReturnType(typeClass, new CodeType(typeClass, typeClass.superName), null, methodName,
								parameterTypes);
					}
					// 没有该方法，又没有父类，则抛出方法找不到异常
					throw new RuntimeException(String.format(
							"The method could not be found!inClass:[%s], type:[%s], methodName:[%s], parameterTypes:%s",
							clazz.getClassName(), type, methodName, parameterTypes));

				}

			} else {// 如果是本地类型，则通过反射进行推导
				// 这里一般都是直接推导到底，因为shy可以调java,而java不一定能直接调用shy
				return NativeLinker.getReturnType(clazz, type, members, methodName, parameterTypes);
			}
		}

		throw new RuntimeException("Cannot deduce returned type!");

	}

}
