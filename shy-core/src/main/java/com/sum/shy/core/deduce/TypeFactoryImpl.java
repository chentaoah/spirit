package com.sum.shy.core.deduce;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.deduce.FastDeducer;
import com.sum.shy.api.deduce.TypeFactory;
import com.sum.shy.lib.Assert;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.clazz.IType;
import com.sum.shy.pojo.common.Context;
import com.sum.shy.pojo.common.StaticType;
import com.sum.shy.pojo.element.Statement;
import com.sum.shy.pojo.element.Token;
import com.sum.shy.utils.TypeUtils;

public class TypeFactoryImpl implements TypeFactory {

	public static FastDeducer deducer = ProxyFactory.get(FastDeducer.class);

	@Override
	public IType create(String className) {// 一般来说，className可以直接反应出大部分属性
		IType type = new IType();
		type.setClassName(className);
		type.setSimpleName(TypeUtils.getSimpleName(className));
		type.setTypeName(TypeUtils.getTypeName(className));
		type.setPrimitive(TypeUtils.isPrimitive(className));
		type.setArray(TypeUtils.isArray(className));
		type.setNull(false);
		type.setWildcard(false);
		type.setNative(!Context.get().contains(TypeUtils.getTargetName(className)));
		type.setModifiers(IType.PUBLIC_MODIFIERS);
		return type;
	}

	@Override
	public IType create(IClass clazz, Token token) {
		if (token.isType()) {
			IType type = new IType();
			if (token.value instanceof String) {// String // String[] //? //T,K
				String simpleName = token.getValue();
				if ("?".equals(simpleName)) {// 未知类型
					return StaticType.WILDCARD_TYPE;
				} else {
					type = clazz.getTypeVariable(simpleName);// 泛型参数
					if (type == null)
						type = create(clazz.findImport(simpleName));// 一般类型
				}
			} else if (token.value instanceof Statement) {// List<String> //
															// Class<?>
				Statement stmt = token.getValue();
				String simpleName = stmt.getStr(0);// 前缀
				type = create(clazz.findImport(simpleName));
				type.setGenericTypes(getGenericTypes(clazz, stmt));
			}
			return type;

		} else if (token.isArrayInit() || token.isTypeInit() || token.isCast()) {
			return create(clazz, token.getSimpleName());

		} else if (token.isValue()) {// 1, 1.1, "xxxx"
			return getValueType(clazz, token);
		}
		return null;
	}

	public List<IType> getGenericTypes(IClass clazz, Statement stmt) {
		List<IType> genericTypes = new ArrayList<>();
		for (int i = 1; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isType())
				genericTypes.add(create(clazz, token));
		}
		return genericTypes;
	}

	public IType getValueType(IClass clazz, Token token) {
		if (token.isBool()) {
			return StaticType.BOOLEAN_TYPE;
		} else if (token.isChar()) {
			return StaticType.CHAR_TYPE;
		} else if (token.isInt()) {
			return StaticType.INT_TYPE;
		} else if (token.isLong()) {
			return StaticType.LONG_TYPE;
		} else if (token.isDouble()) {
			return StaticType.DOUBLE_TYPE;
		} else if (token.isNull()) {
			return StaticType.NULL_TYPE;
		} else if (token.isStr()) {
			return StaticType.STRING_TYPE;
		} else if (token.isList()) {
			return getListType(clazz, token);
		} else if (token.isMap()) {
			return getMapType(clazz, token);
		}
		return null;
	}

	public IType getListType(IClass clazz, Token token) {
		Statement stmt = token.getValue();
		List<Statement> stmts = stmt.subStmt(1, stmt.size() - 1).split(",");
		return create(List.class, getGenericType(clazz, stmts));
	}

	public IType getMapType(IClass clazz, Token token) {
		Statement stmt = token.getValue();
		List<Statement> keyStmts = new ArrayList<>();
		List<Statement> valueStmts = new ArrayList<>();
		for (Statement subStmt : stmt.subStmt(1, stmt.size() - 1).split(",")) {
			List<Statement> subStmts = subStmt.split(":");
			keyStmts.add(subStmts.get(0));
			valueStmts.add(subStmts.get(1));
		}
		return create(Map.class, getGenericType(clazz, keyStmts), getGenericType(clazz, valueStmts));
	}

	public IType getGenericType(IClass clazz, List<Statement> stmts) {

		if (stmts.size() == 0)
			return StaticType.OBJECT_TYPE;

		IType genericType = null;
		for (Statement subStmt : stmts) {
			IType wrappedType = deducer.derive(clazz, subStmt).getWrappedType();
			if (genericType == null) {
				genericType = wrappedType;
				continue;
			}
			if (wrappedType.isMatch(genericType)) {// 更抽象则替换
				genericType = wrappedType;

			} else if (!genericType.isMatch(wrappedType)) {// 不同则使用Object
				genericType = StaticType.OBJECT_TYPE;
				break;
			}
		}
		Assert.notNull(genericType, "Generic type cannot be null!");
		return genericType;
	}

}
