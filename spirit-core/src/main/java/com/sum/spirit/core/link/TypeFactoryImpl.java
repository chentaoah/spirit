package com.sum.spirit.core.link;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.deduce.FastDeducer;
import com.sum.spirit.api.link.TypeFactory;
import com.sum.spirit.lib.Assert;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.pojo.common.Context;
import com.sum.spirit.pojo.common.TypeTable;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.utils.TypeUtils;

@Component
@Primary
public class TypeFactoryImpl implements TypeFactory {

	@Autowired
	public FastDeducer deducer;

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
		type.setModifiers(Constants.PUBLIC_MODIFIERS);
		return type;
	}

	@Override
	public IType create(IClass clazz, Token token) {
		if (token.isType()) {
			return doCreate(clazz, token);

		} else if (token.isAnnotation() || token.isArrayInit() || token.isTypeInit() || token.isCast()) {
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
			Statement statement = token.getValue(); // List<String> // Class<?>
			String simpleName = statement.getStr(0);
			IType type = create(clazz.findImport(simpleName));
			type.setGenericTypes(getGenericTypes(clazz, statement));
			return type;
		}

		throw new RuntimeException("Unknown token value type!");
	}

	public List<IType> getGenericTypes(IClass clazz, Statement statement) {
		List<IType> genericTypes = new ArrayList<>();
		for (int i = 1; i < statement.size(); i++) {
			Token token = statement.getToken(i);
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
		Statement statement = token.getValue();
		List<Statement> statements = statement.subStmt(1, statement.size() - 1).split(",");
		return create(TypeTable.LIST_TYPE.getClassName(), getGenericType(clazz, statements));
	}

	public IType getMapType(IClass clazz, Token token) {
		Statement statement = token.getValue();
		List<Statement> keyStatements = new ArrayList<>();
		List<Statement> valueStatements = new ArrayList<>();
		for (Statement subStatement : statement.subStmt(1, statement.size() - 1).split(",")) {
			List<Statement> subStatements = subStatement.split(":");
			keyStatements.add(subStatements.get(0));
			valueStatements.add(subStatements.get(1));
		}
		return create(TypeTable.MAP_TYPE.getClassName(), getGenericType(clazz, keyStatements), getGenericType(clazz, valueStatements));
	}

	public IType getGenericType(IClass clazz, List<Statement> statements) {

		if (statements.size() == 0)
			return TypeTable.OBJECT_TYPE;

		IType genericType = null;
		for (Statement statement : statements) {
			IType wrappedType = deducer.derive(clazz, statement).getWrappedType();
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
