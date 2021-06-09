package com.gitee.spirit.core.compile;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.common.enums.AccessLevelEnum;
import com.gitee.spirit.common.enums.PrimitiveEnum;
import com.gitee.spirit.core.api.TypeDerivator;
import com.gitee.spirit.core.clazz.AbstractTypeFactory;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.clazz.utils.CommonTypes;
import com.gitee.spirit.core.clazz.utils.TypeUtils;
import com.gitee.spirit.core.compile.derivator.FragmentDeducer;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.Token;

import cn.hutool.core.lang.Assert;

@Primary
@Component
public class AppTypeFactory extends AbstractTypeFactory {

	@Autowired
	public AppClassLoader classLoader;
	@Autowired
	public FragmentDeducer deducer;
	@Autowired
	public TypeDerivator derivator;

	@Override
	public IType create(String className) {// 一般来说，className可以直接反应出大部分属性
		IType type = new IType();
		type.setClassName(className);
		type.setSimpleName(TypeUtils.getSimpleName(className));
		type.setTypeName(TypeUtils.getTypeName(className));
		type.setPrimitive(PrimitiveEnum.isPrimitive(className));
		type.setArray(TypeUtils.isArray(className));
		type.setNull(false);
		type.setWildcard(false);
		type.setNative(!classLoader.contains(TypeUtils.getTargetName(className)));
		type.setModifiers(AccessLevelEnum.PUBLIC.value);
		return type;
	}

	@Override
	public IType create(IClass clazz, Token token) {
		if (token.isType()) {
			return doCreate(clazz, token);

		} else if (token.isAnnotation() || token.isArrayInit() || token.isTypeInit() || token.isCast()) {
			return create(clazz, (String) token.attr(Attribute.SIMPLE_NAME));

		} else if (token.isLiteral()) {// 1, 1.1, "xxxx"
			return getValueType(clazz, token);
		}
		return null;
	}

	public IType doCreate(IClass clazz, Token token) {
		if (token.value instanceof String) {// String // String[] //? //T,K
			String simpleName = token.getValue();
			if ("?".equals(simpleName)) {
				return CommonTypes.WILDCARD;// ?
			}
			if (clazz.getTypeVariableIndex(simpleName) >= 0) {
				return createTypeVariable(simpleName);// T or K
			}
			return create(clazz.findClassName(simpleName));

		} else if (token.value instanceof Statement) {
			Statement statement = token.getValue(); // List<String> // Class<?>
			String simpleName = statement.getStr(0);
			IType type = create(clazz.findClassName(simpleName));
			type.setGenericTypes(getGenericTypes(clazz, statement));
			return type;
		}
		throw new RuntimeException("Unknown token value type!");
	}

	public List<IType> getGenericTypes(IClass clazz, Statement statement) {
		List<IType> genericTypes = new ArrayList<>();
		for (int i = 1; i < statement.size(); i++) {
			Token token = statement.get(i);
			if (token.isType()) {
				genericTypes.add(create(clazz, token));
			}
		}
		return genericTypes;
	}

	public IType getValueType(IClass clazz, Token token) {
		if (token.isBoolean()) {
			return CommonTypes.BOOLEAN;
		} else if (token.isChar()) {
			return CommonTypes.CHAR;
		} else if (token.isInt()) {
			return CommonTypes.INT;
		} else if (token.isLong()) {
			return CommonTypes.LONG;
		} else if (token.isDouble()) {
			return CommonTypes.DOUBLE;
		} else if (token.isNull()) {
			return CommonTypes.NULL;
		} else if (token.isString()) {
			return CommonTypes.STRING;
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
		return create(CommonTypes.LIST.getClassName(), getGenericType(clazz, statements));
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
		return create(CommonTypes.MAP.getClassName(), getGenericType(clazz, keyStatements), getGenericType(clazz, valueStatements));
	}

	public IType getGenericType(IClass clazz, List<Statement> statements) {
		// 如果没有元素，则返回Object类型
		if (statements.size() == 0) {
			return CommonTypes.OBJECT;
		}
		IType genericType = null;
		for (Statement statement : statements) {
			IType boxType = deducer.derive(clazz, statement).toBox();
			if (genericType == null) {
				genericType = boxType;
				continue;
			}
			if (derivator.isMoreAbstract(boxType, genericType)) {// 更抽象则替换
				genericType = boxType;

			} else if (!derivator.isMoreAbstract(genericType, boxType)) {// 不同则使用Object
				genericType = CommonTypes.OBJECT;
				break;
			}
		}
		Assert.notNull(genericType, "Generic type cannot be null!");
		return genericType;
	}

}
