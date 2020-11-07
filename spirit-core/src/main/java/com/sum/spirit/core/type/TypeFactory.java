package com.sum.spirit.core.type;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.core.SystemClassLoader;
import com.sum.spirit.core.visit.FastDeducer;
import com.sum.spirit.lib.Assert;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.enums.ModifierEnum;
import com.sum.spirit.pojo.enums.TypeEnum;
import com.sum.spirit.utils.TypeUtils;

@Component
@Primary
public class TypeFactory extends AbsTypeFactory {

	@Autowired
	public SystemClassLoader systemClassLoader;
	@Autowired
	public FastDeducer deducer;
	@Autowired
	public ClassLinker linker;

	@Override
	public IType create(String className) {// 一般来说，className可以直接反应出大部分属性
		IType type = new IType();
		type.setClassName(className);
		type.setSimpleName(TypeUtils.getSimpleName(className));
		type.setTypeName(TypeUtils.getTypeName(className));
		type.setPrimitive(TypeEnum.isPrimitive(className));
		type.setArray(TypeUtils.isArray(className));
		type.setNull(false);
		type.setWildcard(false);
		type.setNative(!systemClassLoader.isLoaded(TypeUtils.getTargetName(className)));
		type.setModifiers(ModifierEnum.PUBLIC.value);
		return type;
	}

	@Override
	public IType create(IClass clazz, Token token) {
		if (token.isType()) {
			return doCreate(clazz, token);

		} else if (token.isAnnotation() || token.isArrayInit() || token.isTypeInit() || token.isCast()) {
			return create(clazz, (String) token.getAttribute(AttributeEnum.SIMPLE_NAME));

		} else if (token.isValue()) {// 1, 1.1, "xxxx"
			return getValueType(clazz, token);
		}
		return null;
	}

	public IType doCreate(IClass clazz, Token token) {

		if (token.value instanceof String) {// String // String[] //? //T,K

			String simpleName = token.getValue();

			if ("?".equals(simpleName))
				return TypeEnum.WILDCARD.value;// ?

			if (clazz.getTypeVariableIndex(simpleName) >= 0)
				return createTypeVariable(simpleName);// T or K

			return create(clazz.getClassName(simpleName));

		} else if (token.value instanceof Statement) {
			Statement statement = token.getValue(); // List<String> // Class<?>
			String simpleName = statement.getStr(0);
			IType type = create(clazz.getClassName(simpleName));
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
			return TypeEnum.BOOLEAN.value;
		} else if (token.isChar()) {
			return TypeEnum.CHAR.value;
		} else if (token.isInt()) {
			return TypeEnum.INT.value;
		} else if (token.isLong()) {
			return TypeEnum.LONG.value;
		} else if (token.isDouble()) {
			return TypeEnum.DOUBLE.value;
		} else if (token.isNull()) {
			return TypeEnum.NULL.value;
		} else if (token.isStr()) {
			return TypeEnum.STRING.value;
		} else if (token.isList()) {
			return getListType(clazz, token);
		} else if (token.isMap()) {
			return getMapType(clazz, token);
		}
		return null;
	}

	public IType getListType(IClass clazz, Token token) {
		Statement statement = token.getValue();
		List<Statement> statements = statement.subStmt(1, statement.size() - 1).splitStmt(",");
		return create(TypeEnum.LIST.value.getClassName(), getGenericType(clazz, statements));
	}

	public IType getMapType(IClass clazz, Token token) {
		Statement statement = token.getValue();
		List<Statement> keyStatements = new ArrayList<>();
		List<Statement> valueStatements = new ArrayList<>();
		for (Statement subStatement : statement.subStmt(1, statement.size() - 1).splitStmt(",")) {
			List<Statement> subStatements = subStatement.splitStmt(":");
			keyStatements.add(subStatements.get(0));
			valueStatements.add(subStatements.get(1));
		}
		return create(TypeEnum.MAP.value.getClassName(), getGenericType(clazz, keyStatements), getGenericType(clazz, valueStatements));
	}

	public IType getGenericType(IClass clazz, List<Statement> statements) {

		if (statements.size() == 0)
			return TypeEnum.OBJECT.value;

		IType genericType = null;
		for (Statement statement : statements) {
			IType wrappedType = deducer.derive(clazz, statement).getWrappedType();
			if (genericType == null) {
				genericType = wrappedType;
				continue;
			}
			if (linker.isMoreAbstract(wrappedType, genericType)) {// 更抽象则替换
				genericType = wrappedType;

			} else if (!linker.isMoreAbstract(genericType, wrappedType)) {// 不同则使用Object
				genericType = TypeEnum.OBJECT.value;
				break;
			}
		}
		Assert.notNull(genericType, "Generic type cannot be null!");
		return genericType;
	}

}
