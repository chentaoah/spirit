package com.sum.spirit.core.compile.linker;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.AttributeEnum;
import com.sum.spirit.common.enums.ModifierEnum;
import com.sum.spirit.common.enums.PrimitiveEnum;
import com.sum.spirit.core.api.ClassLinker;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.clazz.utils.TypeUtils;
import com.sum.spirit.core.compile.AppClassLoader;
import com.sum.spirit.core.compile.action.FastDeducer;
import com.sum.spirit.core.compile.deduce.ImportManager;
import com.sum.spirit.core.compile.deduce.TypeDerivator;
import com.sum.spirit.core.compile.entity.StaticTypes;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;

import cn.hutool.core.lang.Assert;

@Component
@Primary
public class TypeFactory extends AbstractTypeFactory {

	@Autowired
	public AppClassLoader classLoader;
	@Autowired
	public FastDeducer deducer;
	@Autowired
	public ClassLinker linker;
	@Autowired
	public ImportManager manager;
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
		type.setModifiers(ModifierEnum.PUBLIC.value);
		return type;
	}

	@Override
	public IType create(IClass clazz, Token token) {
		if (token.isType()) {
			return doCreate(clazz, token);

		} else if (token.isAnnotation() || token.isArrayInit() || token.isTypeInit() || token.isCast()) {
			return create(clazz, (String) token.attr(AttributeEnum.SIMPLE_NAME));

		} else if (token.isValue()) {// 1, 1.1, "xxxx"
			return getValueType(clazz, token);
		}
		return null;
	}

	public IType doCreate(IClass clazz, Token token) {
		if (token.value instanceof String) {// String // String[] //? //T,K
			String simpleName = token.getValue();
			if ("?".equals(simpleName)) {
				return StaticTypes.WILDCARD;// ?
			}
			if (clazz.getTypeVariableIndex(simpleName) >= 0) {
				return createTypeVariable(simpleName);// T or K
			}
			return create(manager.findClassName(clazz, simpleName));

		} else if (token.value instanceof Statement) {
			Statement statement = token.getValue(); // List<String> // Class<?>
			String simpleName = statement.getStr(0);
			IType type = create(manager.findClassName(clazz, simpleName));
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
		if (token.isBool()) {
			return StaticTypes.BOOLEAN;
		} else if (token.isChar()) {
			return StaticTypes.CHAR;
		} else if (token.isInt()) {
			return StaticTypes.INT;
		} else if (token.isLong()) {
			return StaticTypes.LONG;
		} else if (token.isDouble()) {
			return StaticTypes.DOUBLE;
		} else if (token.isNull()) {
			return StaticTypes.NULL;
		} else if (token.isStr()) {
			return StaticTypes.STRING;
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
		return create(StaticTypes.LIST.getClassName(), getGenericType(clazz, statements));
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
		return create(StaticTypes.MAP.getClassName(), getGenericType(clazz, keyStatements), getGenericType(clazz, valueStatements));
	}

	public IType getGenericType(IClass clazz, List<Statement> statements) {
		// 如果没有元素，则返回Object类型
		if (statements.size() == 0) {
			return StaticTypes.OBJECT;
		}
		IType genericType = null;
		for (Statement statement : statements) {
			IType wrappedType = derivator.getBoxType(deducer.derive(clazz, statement));
			if (genericType == null) {
				genericType = wrappedType;
				continue;
			}
			if (linker.isMoreAbstract(wrappedType, genericType)) {// 更抽象则替换
				genericType = wrappedType;

			} else if (!linker.isMoreAbstract(genericType, wrappedType)) {// 不同则使用Object
				genericType = StaticTypes.OBJECT;
				break;
			}
		}
		Assert.notNull(genericType, "Generic type cannot be null!");
		return genericType;
	}

}
