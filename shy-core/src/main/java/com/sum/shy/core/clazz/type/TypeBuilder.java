package com.sum.shy.core.clazz.type;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IType;

public class TypeBuilder {

	public static String build(IClass clazz, IType type) {

		if (type.isWildcard())
			return "?";

		String finalName = clazz.addImport(type.getTargetName()) ? type.getSimpleName() : type.getTypeName();

		if (type.isGenericType()) {// 泛型
			List<String> strs = new ArrayList<>();
			for (IType genericType : type.getGenericTypes())
				strs.add(build(clazz, genericType));
			return finalName + "<" + Joiner.on(", ").join(strs) + ">";
		}

		return finalName;

	}

}