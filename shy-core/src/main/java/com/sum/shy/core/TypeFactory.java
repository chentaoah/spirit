package com.sum.shy.core;

import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.lexical.SemanticDelegate;
import com.sum.shy.core.utils.TypeUtils;

public class TypeFactory {

	public static final Pattern BASIC_TYPE_PATTERN = Pattern.compile("^(" + SemanticDelegate.BASIC_TYPE_ENUM + ")$");

	public static IType resolve(IClass clazz, String text) {
		return resolve(clazz, SemanticDelegate.getToken(text));
	}

	public static IType resolve(IClass clazz, Token token) {
		if (token.isType()) {
			if (token.value instanceof String) {// String // String[]
				IType type = new IType();
				String simpleName = (String) token.value;
				if ("?".equals(simpleName)) {// 未知类型
					type.className = WildcardType.class.getName();
					type.simpleName = simpleName;
					type.typeName = simpleName;
					type.isPrimitive = false;
					type.isArray = false;
					type.isGenericType = false;
					type.genericTypes = new ArrayList<>();
					type.isWildcard = true;
					type.from = clazz;
					type.isNative = true;

				} else {// 一般类型
					type.className = clazz.findImport(simpleName);
					type.simpleName = simpleName;
					type.typeName = TypeUtils.getTypeName(simpleName);
					type.isPrimitive = BASIC_TYPE_PATTERN.matcher(type.className).matches();
					type.isArray = TypeUtils.isArray(simpleName);
					type.isGenericType = false;
					type.genericTypes = new ArrayList<>();
					type.isWildcard = false;
					type.from = clazz;
					type.isNative = Context.get().contains(type.className);

				}
				return type;

			} else if (token.value instanceof Stmt) {// List<String> // Class<?>

			}

		} else if (token.isArrayInit() || token.isTypeInit() || token.isCast()) {

		} else if (token.isValue()) {// 1, 1.1, "xxxx"

		}

		return null;
	}

}
