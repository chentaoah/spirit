package com.sum.spirit.java.linker;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.api.ClassLinker;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.clazz.utils.TypeBuilder;
import com.sum.spirit.core.clazz.utils.TypeVisiter;
import com.sum.spirit.core.compile.deduce.TypeFactory;
import com.sum.spirit.core.compile.entity.StaticTypes;

@Component
public class NativeFactory extends TypeFactory {

	@Autowired
	public ClassLinker linker;

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
			return StaticTypes.WILDCARD;

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

	public IType populate(IType type, IType targetType) {// 根据全局类型，进行填充
		return TypeVisiter.visit(targetType, eachType -> {
			if (eachType.isTypeVariable()) {
				int index = linker.getTypeVariableIndex(type, eachType.getGenericName());
				if (index >= 0) {
					return TypeBuilder.copy(type.getGenericTypes().get(index));
				}
			}
			return eachType;
		});
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
		return TypeVisiter.visit(targetType, parameterType, (eachType, referType) -> {
			if (eachType.isTypeVariable()) {
				String genericName = eachType.getGenericName();
				if (qualifyingTypes.containsKey(genericName)) {// 如果已经存在了，则必须统一
					IType existType = qualifyingTypes.get(genericName);
					if (!existType.equals(parameterType)) {
						throw new RuntimeException("Parameter qualification types are not uniform!");
					}
					referType = TypeBuilder.copy(referType);
					return referType;

				} else {
					referType = TypeBuilder.copy(referType);
					qualifyingTypes.put(genericName, referType);
					return referType;
				}
			}
			return eachType;
		});
	}

	public IType populate(Map<String, IType> qualifyingTypes, IType targetType) {
		return TypeVisiter.visit(targetType, eachType -> {
			if (eachType.isTypeVariable()) {
				return qualifyingTypes.get(targetType.getGenericName());
			}
			return eachType;
		});
	}

}
