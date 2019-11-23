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

public class InvokeVisiter {

	/**
	 * 遍历所有的字段和方法,推导出最终的类型
	 * 
	 * @param classes
	 */
	public static void visitClasses(Map<String, CtClass> classes) {
		for (CtClass clazz : classes.values()) {
			for (Element element : clazz.getAllElement()) {
				element.setType(visitElement(clazz, element));
			}
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
			if (element instanceof CtField) {// 如果是字段
				Stmt stmt = ((CtField) element).stmt;
				visit(clazz, stmt);// 推导类型
				type = FastDerivator.getType(clazz, stmt);// 快速推导

			} else if (element instanceof CtMethod) {// 如果是方法
				Holder<Type> holder = new Holder<>();
				MethodResolver.resolve(clazz, (CtMethod) element, new Handler() {
					@Override
					public Object handle(CtClass clazz, CtMethod method, String indent, String block, Line line,
							Stmt stmt) {
						if (stmt.isReturn()) {
							holder.obj = FastDerivator.getType(clazz, stmt);
						}
						return null;
					}
				});
				type = holder.obj != null ? holder.obj : new CodeType(clazz, "void");
			}

		}
		// 解锁
		element.unLock();
		return type;
	}

	public static void visit(CtClass clazz, Stmt stmt) {
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isInvokeInit()) {
				token.setReturnTypeAtt(new CodeType(clazz, token.getTypeNameAtt()));

			} else if (token.isInvokeStatic()) {
				Type type = new CodeType(clazz, token.getTypeNameAtt());
				Type returnType = getReturnType(clazz, type, token.getPropertiesAtt(), token.getMethodNameAtt());
				token.setReturnTypeAtt(returnType);

			} else if (token.isInvokeMember()) {
				Type type = token.getTypeAtt();
				Type returnType = getReturnType(clazz, type, token.getPropertiesAtt(), token.getMethodNameAtt());
				token.setReturnTypeAtt(returnType);

			} else if (token.isInvokeLocal()) {
				Type type = new CodeType(clazz, clazz.typeName);
				Type returnType = getReturnType(clazz, type, null, token.getMethodNameAtt());
				token.setReturnTypeAtt(returnType);

			} else if (token.isInvokeFluent()) {
				Type type = stmt.getToken(i - 1).getReturnTypeAtt();
				Type returnType = getReturnType(clazz, type, token.getPropertiesAtt(), token.getMethodNameAtt());
				token.setReturnTypeAtt(returnType);

			} else if (token.isStaticVar()) {
				Type type = new CodeType(clazz, token.getTypeNameAtt());
				Type returnType = getReturnType(clazz, type, token.getPropertiesAtt(), null);
				token.setReturnTypeAtt(returnType);

			} else if (token.isMemberVar()) {
				Type type = token.getTypeAtt();
				Type returnType = getReturnType(clazz, type, token.getPropertiesAtt(), null);
				token.setReturnTypeAtt(returnType);

			} else if (token.isMemberVarFluent()) {
				Type type = stmt.getToken(i - 1).getReturnTypeAtt();
				Type returnType = getReturnType(clazz, type, token.getPropertiesAtt(), null);
				token.setReturnTypeAtt(returnType);

			}

		}

	}

	private static Type getReturnType(CtClass clazz, Type type, List<String> properties, String methodName) {

		String className = type.getClassName();
		if (Context.get().isFriend(className)) {// 如果是友元，则字面意思进行推导
			CtClass clazz1 = Context.get().findClass(className);
			if (properties != null && properties.size() > 0) {
				String property = properties.remove(0);// 获取第一个属性
				CtField field = clazz1.findField(property);
				Type returnType = visitElement(clazz1, field);// 可能字段类型还需要进行深度推导
				if (properties.size() > 0)
					returnType = getReturnType(clazz1, returnType, properties, methodName);
				return returnType;

			} else if (methodName != null) {
				CtMethod method = clazz1.findMethod(methodName);
				return visitElement(clazz1, method);// 可能字段类型还需要进行深度推导
			}

		} else {// 如果是本地类型，则通过反射进行推导

		}

		return null;
	}

}
