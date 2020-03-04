package com.sum.shy.core.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.doc.Element;
import com.sum.shy.core.doc.Stmt;
import com.sum.shy.core.doc.Token;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.type.CodeType;
import com.sum.shy.core.type.api.IType;

public class IMethod extends AbsMember {

	public List<IParameter> parameters = new ArrayList<>();

	/**
	 * 构造方法，这个时候，还不知道方法的返回类型是什么
	 * 
	 * @param clazz       这里需要传入的原因是要确定参数的类型
	 * @param annotations
	 * @param isStatic
	 * @param element
	 */
	public IMethod(IClass clazz, List<Element> annotations, boolean isStatic, Element element) {
		super(annotations, isStatic, element);
		Token methodToken = element.findToken(Constants.LOCAL_METHOD_TOKEN);
		name = methodToken.getMemberNameAtt();
		List<Stmt> subStmts = methodToken.getStmt().subStmt("(", ")").split(",");
		for (Stmt paramStmt : subStmts) {
			for (Token token : paramStmt.tokens) {
				IParameter parameter = new IParameter();
				if (token.isAnnotation()) {// TODO 这里暂时不处理注解
					continue;
				} else if (token.isType()) {
					parameter.type = new CodeType(clazz, token);
				} else if (token.isVar()) {
					parameter.name = token.toString();
				}
				parameters.add(parameter);
			}

		}

	}

	public List<IParameter> getParameters() {
		return parameters;
	}

	public boolean isMatch(String methodName, List<IType> parameterTypes) {
		if (name.equals(methodName) && parameters.size() == parameterTypes.size()) {
			int count = 0;
			for (IParameter parameter : parameters) {
				if (!parameter.type.equals(parameterTypes.get(count++)))
					return false;
			}
			return true;
		}
		return false;
	}

}
