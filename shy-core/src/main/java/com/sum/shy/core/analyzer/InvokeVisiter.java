package com.sum.shy.core.analyzer;

import java.util.List;
import java.util.Map;

import com.sum.shy.core.api.Element;
import com.sum.shy.core.api.Handler;
import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.CtField;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Holder;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.utils.ReflectUtils;
import com.sum.shy.library.Collection;
import com.sum.shy.library.StringUtils;

public class InvokeVisiter {

	/**
	 * 遍历所有的字段和方法,推导出最终的类型
	 * 
	 * @param classes
	 */
	public static void visitClasses(Map<String, CtClass> classes) {
		for (CtClass clazz : classes.values()) {
			visitClass(clazz);

		}
	}

	/**
	 * 推导类
	 * 
	 * @param clazz
	 */
	public static void visitClass(CtClass clazz) {
		for (Element element : clazz.getAllElement()) {
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
	public static Type visitElement(CtClass clazz, Element element) {
		// 上锁
		element.lock();
		Type type = element.getType();
		if (type == null) {
//			Context.get().currentClass = clazz;
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
						if (stmt.isReturn())
							holder.obj = FastDerivator.getType(clazz, stmt);
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
		if (token.isInvokeInit()) {
			token.setReturnTypeAtt(new CodeType(clazz, token.getTypeNameAtt()));

		} else if (token.isInvokeStatic()) {
			Type type = new CodeType(clazz, token.getTypeNameAtt());
			Type returnType = getReturnType(clazz, type, token.getMembersAtt(), token.getMethodNameAtt());
			token.setReturnTypeAtt(returnType);

		} else if (token.isInvokeMember()) {
			Type type = token.getTypeAtt();
			Type returnType = getReturnType(clazz, type, token.getMembersAtt(), token.getMethodNameAtt());
			token.setReturnTypeAtt(returnType);

		} else if (token.isInvokeLocal()) {// 本地调用
			Type type = new CodeType(clazz, clazz.typeName);
			Type returnType = getReturnType(clazz, type, null, token.getMethodNameAtt());
			token.setReturnTypeAtt(returnType);

		} else if (token.isInvokeFluent()) {
			// 如果是判空语句,则向前倒两位 like obj?.do()
			Token lastToken = stmt.getToken(index - 1);
			if (lastToken.isOperator() && "?".equals(lastToken.value))
				lastToken = stmt.getToken(index - 2);
			// ?号前面可能是变量也可能是方法调用
			Type type = lastToken.isVar() ? lastToken.getTypeAtt() : lastToken.getReturnTypeAtt();
			Type returnType = getReturnType(clazz, type, token.getMembersAtt(), token.getMethodNameAtt());
			token.setReturnTypeAtt(returnType);

		} else if (token.isStaticVar()) {
			Type type = new CodeType(clazz, token.getTypeNameAtt());
			Type returnType = getReturnType(clazz, type, token.getMembersAtt(), null);
			token.setReturnTypeAtt(returnType);

		} else if (token.isMemberVar()) {
			Type type = token.getTypeAtt();
			Type returnType = getReturnType(clazz, type, token.getMembersAtt(), null);
			token.setReturnTypeAtt(returnType);

		} else if (token.isMemberVarFluent()) {
			Type type = stmt.getToken(index - 1).getReturnTypeAtt();
			Type returnType = getReturnType(clazz, type, token.getMembersAtt(), null);
			token.setReturnTypeAtt(returnType);

		} else if (token.isQuickIndex()) {
			Type type = token.getTypeAtt();
			Type returnType = getReturnType(clazz, type, token.getMembersAtt(), token.getMethodNameAtt());
			token.setReturnTypeAtt(returnType);

		}
		// 内部可能还需要推导
		if (token.hasSubStmt()) {
			visitStmt(clazz, (Stmt) token.value);
		}

	}

	public static Type getReturnType(CtClass clazz, Type type, List<String> members, String methodName) {

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
			if (Context.get().isFriend(className)) {// 如果是友元，则字面意思进行推导
				CtClass typeClass = Context.get().findClass(className);// 获取友元
				if (members != null && members.size() > 0) {
					String member = members.remove(0);// 获取第一个属性
					if (typeClass.existField(member)) {
						CtField field = typeClass.findField(member);
						Type returnType = visitElement(typeClass, field);// 可能字段类型还需要进行深度推导
						if (members.size() > 0 || methodName != null)
							returnType = getReturnType(typeClass, returnType, members, methodName);
						return returnType;

					} else if (StringUtils.isNotEmpty(typeClass.superName)) {// 如果不存在该属性，则向上寻找
						// 父类可能是java里面的类
						Type returnType = InvokeVisiter.getReturnType(typeClass,
								new CodeType(typeClass, typeClass.superName), Collection.newArrayList(member), null);
						if (members.size() > 0 || methodName != null)
							returnType = getReturnType(typeClass, returnType, members, methodName);
						return returnType;

					}

				} else if (methodName != null) {
					if (typeClass.existMethod(methodName)) {
						CtMethod method = typeClass.findMethod(methodName);
						return visitElement(typeClass, method);// 可能字段类型还需要进行深度推导

					} else if (StringUtils.isNotEmpty(typeClass.superName)) {
						return InvokeVisiter.getReturnType(typeClass, new CodeType(typeClass, typeClass.superName),
								null, methodName);
					}

				}

			} else {// 如果是本地类型，则通过反射进行推导
				// 这里一般都是直接推导到底，因为shy可以调java,而java不一定能直接调用shy
				return ReflectUtils.getReturnType(clazz, type, members, methodName);
			}
		}

		return null;
	}

}
