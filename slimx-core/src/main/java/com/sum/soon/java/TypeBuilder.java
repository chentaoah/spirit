package com.sum.soon.java;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.sum.soon.pojo.clazz.IClass;
import com.sum.soon.pojo.clazz.IType;

public class TypeBuilder {

	public static String build(IClass clazz, IType type) {

		if (type.isWildcard())
			return "?";

		if (type.isTypeVariable())
			return type.getGenericName();// T K

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
