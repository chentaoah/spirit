package com.sum.spirit.java.core.link;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sum.spirit.core.d.type.TypeFactory;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.enums.TypeEnum;
import com.sum.spirit.utils.TypeBuilder;
import com.sum.spirit.utils.TypeVisiter;

@Component
public class NativeFactory extends TypeFactory {

	public IType create(Class<?> clazz) {
		IType type = create(clazz.getName());
		TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
		if (typeVariables != null && typeVariables.length > 0) {
			List<IType> genericTypes = new ArrayList<>();
			for (TypeVariable<?> typeVariable : typeVariables) {
				genericTypes.add(createTypeVariable(typeVariable.toString()));
			}
			// 不可修改集合
			type.setGenericTypes(Collections.unmodifiableList(genericTypes));
		}
		return type;
	}

	public IType create(Type nativeType) {
		if (nativeType instanceof Class) {// String
			return create((Class<?>) nativeType);

		} else if (nativeType instanceof WildcardType) {// ?
			return TypeEnum.Wildcard.value;

		} else if (nativeType instanceof TypeVariable) {// T or K
			return createTypeVariable(nativeType.toString());

		} else if (nativeType instanceof ParameterizedType) {// List<T>
			ParameterizedType parameterizedType = (ParameterizedType) nativeType;
			Class<?> rawType = (Class<?>) parameterizedType.getRawType();
			List<IType> genericTypes = new ArrayList<>();
			for (Type actualType : parameterizedType.getActualTypeArguments()) {
				genericTypes.add(create(actualType));
			}
			return create(rawType.getName(), genericTypes);
		}
		throw new RuntimeException("Unknown type!");
	}

	@Override
	public boolean checkIndex(IType type, int index) {// 父类校验，这里不校验
		return index >= 0;
	}

	public IType populate(IType type, IType parameterType, IType targetType) {
		return populate(type, parameterType, targetType, new HashMap<>());
	}

	public IType populate(IType type, IType parameterType, IType targetType, Map<String, IType> qualifyingTypes) {
		// 先使用类型填充
		targetType = populate(type, targetType);
		// 然后使用参数类型填充
		targetType = populate(parameterType, targetType, qualifyingTypes);
		// 返回类型
		return targetType;
	}

	public IType populate(IType type, Map<String, IType> qualifyingTypes, IType targetType) {
		// 先使用类型填充
		targetType = populate(type, targetType);
		// 再用限定类型填充
		targetType = populate(qualifyingTypes, targetType);
		// 返回类型
		return targetType;
	}

	public IType populate(IType parameterType, IType targetType, Map<String, IType> qualifyingTypes) {
		// 使用匿名表达式
		return new TypeVisiter().visit(parameterType, targetType, (rawType, index, referenceType, currentType) -> {
			if (currentType.isTypeVariable()) {
				String genericName = currentType.getGenericName();
				// 如果已经存在了，则必须统一
				if (qualifyingTypes.containsKey(genericName)) {
					IType existType = qualifyingTypes.get(genericName);
					if (!existType.equals(parameterType)) {
						throw new RuntimeException("Parameter qualification types are not uniform!");
					}
					referenceType = TypeBuilder.copy(referenceType);
					return referenceType;

				} else {
					referenceType = TypeBuilder.copy(referenceType);
					qualifyingTypes.put(genericName, referenceType);
					return referenceType;
				}
			}
			return null;
		});
	}

	public IType populate(Map<String, IType> qualifyingTypes, IType targetType) {
		// 使用匿名表达式
		return new TypeVisiter().visit(targetType, (rawType, index, currentType) -> {
			if (currentType.isTypeVariable()) {
				return qualifyingTypes.get(targetType.getGenericName());
			}
			return null;
		});
	}

}
