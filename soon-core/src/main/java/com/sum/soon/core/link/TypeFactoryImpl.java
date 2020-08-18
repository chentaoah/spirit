package com.sum.soon.core.link;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sum.pisces.core.ProxyFactory;
import com.sum.soon.api.deduce.FastDeducer;
import com.sum.soon.api.link.TypeFactory;
import com.sum.soon.lib.Assert;
import com.sum.soon.pojo.clazz.IClass;
import com.sum.soon.pojo.clazz.IType;
import com.sum.soon.pojo.common.Context;
import com.sum.soon.pojo.common.TypeTable;
import com.sum.soon.pojo.element.Statement;
import com.sum.soon.pojo.element.Token;
import com.sum.soon.utils.TypeUtils;

public class TypeFactoryImpl implements TypeFactory {

	public static FastDeducer deducer = ProxyFactory.get(FastDeducer.class);

	@Override
	public IType create(String className) {// 一般来说，className可以直接反应出大部分属性
		IType type = new IType();
		type.setClassName(className);
		type.setSimpleName(TypeUtils.getSimpleName(className));
		type.setTypeName(TypeUtils.getTypeName(className));
		type.setPrimitive(TypeTable.isPrimitive(className));
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
			return doCreate(clazz, token);

		} else if (token.isArrayInit() || token.isTypeInit() || token.isCast()) {
			return create(clazz, token.getSimpleName());

		} else if (token.isValue()) {// 1, 1.1, "xxxx"
			return getValueType(clazz, token);
		}
		return null;
	}

	public IType doCreate(IClass clazz, Token token) {

		if (token.value instanceof String) {// String // String[] //? //T,K

			String simpleName = token.getValue();

			if ("?".equals(simpleName))
				return TypeTable.WILDCARD_TYPE;// ?

			if (clazz.getTypeVariableIndex(simpleName) >= 0)
				return createTypeVariable(simpleName);// T or K

			return create(clazz.findImport(simpleName));

		} else if (token.value instanceof Statement) {
			Statement stmt = token.getValue(); // List<String> // Class<?>
			String simpleName = stmt.getStr(0);
			IType type = create(clazz.findImport(simpleName));
			type.setGenericTypes(getGenericTypes(clazz, stmt));
			return type;
		}

		throw new RuntimeException("Unknown token value type!");
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
			return TypeTable.BOOLEAN_TYPE;
		} else if (token.isChar()) {
			return TypeTable.CHAR_TYPE;
		} else if (token.isInt()) {
			return TypeTable.INT_TYPE;
		} else if (token.isLong()) {
			return TypeTable.LONG_TYPE;
		} else if (token.isDouble()) {
			return TypeTable.DOUBLE_TYPE;
		} else if (token.isNull()) {
			return TypeTable.NULL_TYPE;
		} else if (token.isStr()) {
			return TypeTable.STRING_TYPE;
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
			return TypeTable.OBJECT_TYPE;

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
				genericType = TypeTable.OBJECT_TYPE;
				break;
			}
		}
		Assert.notNull(genericType, "Generic type cannot be null!");
		return genericType;
	}

}
