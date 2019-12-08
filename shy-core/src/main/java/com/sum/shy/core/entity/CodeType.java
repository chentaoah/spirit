package com.sum.shy.core.entity;

import com.sum.shy.core.analyzer.SemanticDelegate;

public class CodeType extends AbsType {

	public String className;
	public String simpleName;

	public CodeType(CtClass clazz, Token token) {
		super(clazz);
		resolve(clazz, token);
	}

	public CodeType(CtClass clazz, String type) {
		super(clazz);
		Token token = SemanticDelegate.getToken(type);// 语义分析器,会自动将泛型进行拆分
		resolve(clazz, token);
	}

	private void resolve(CtClass clazz, Token token) {
		if (token.isType()) {
			if (token.value instanceof String) {
				simpleName = (String) token.value;

			} else if (token.value instanceof Stmt) {
				Stmt subStmt = (Stmt) token.value;
				simpleName = subStmt.get(0);// 前缀
				for (int i = 1; i < subStmt.size(); i++) {
					Token subToken = subStmt.getToken(i);
					if (subToken.isType())
						genericTypes.add(new CodeType(clazz, subToken));
				}
			}
			// 查找类名
			className = clazz.findClassName(simpleName);

		} else {
			throw new RuntimeException("Token is not type token!token:[" + token + "]");
		}

	}

	@Override
	public String getClassName() {
		return className;
	}

	@Override
	public String getSimpleName() {
		return simpleName;
	}

}
