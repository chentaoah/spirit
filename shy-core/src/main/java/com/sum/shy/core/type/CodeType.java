package com.sum.shy.core.type;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.lexical.SemanticDelegate;
import com.sum.shy.core.type.api.AbsType;

public class CodeType extends AbsType {

	public String className;
	public String simpleName;

	public CodeType(IClass clazz, Token token) {
		super(clazz);
		resolve(clazz, token);
	}

	public CodeType(IClass clazz, String type) {
		super(clazz);
		Token token = SemanticDelegate.getToken(type);// 语义分析器,会自动将泛型进行拆分
		resolve(clazz, token);
	}

	public CodeType(IClass clazz, String className, String simpleName) {
		super(clazz);
		this.className = className;
		this.simpleName = simpleName;
	}

	public void resolve(IClass clazz, Token token) {
		if (token.isType()) {
			if (token.value instanceof String) {
				simpleName = token.toString();

			} else if (token.value instanceof Stmt) {
				Stmt subStmt = token.getStmt();
				simpleName = subStmt.get(0);// 前缀
				for (int i = 1; i < subStmt.size(); i++) {
					Token subToken = subStmt.getToken(i);
					if (subToken.isType())
						genericTypes.add(new CodeType(clazz, subToken));
				}
			}
			// 查找类名
			className = clazz.findImport(simpleName);

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
