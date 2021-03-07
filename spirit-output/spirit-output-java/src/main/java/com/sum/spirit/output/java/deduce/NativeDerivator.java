package com.sum.spirit.output.java.deduce;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.api.ClassLinker;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.clazz.utils.TypeBuilder;
import com.sum.spirit.core.clazz.utils.TypeVisiter;
import com.sum.spirit.core.compile.deduce.TypeDerivator;

@Component
public class NativeDerivator extends TypeDerivator {

	@Autowired
	public ClassLinker linker;

	@Override
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

	public IType populateByParameter(IType type, IType parameterType, IType targetType) {
		return populateQualifying(type, parameterType, targetType, new HashMap<>());
	}

	public IType populateQualifying(IType type, IType parameterType, IType targetType, Map<String, IType> qualifyingTypes) {
		// 先使用类型填充
		targetType = populate(type, targetType);
		// 然后使用参数类型填充
		targetType = populateQualifying(parameterType, targetType, qualifyingTypes);
		// 返回类型
		return targetType;
	}

	public IType populateQualifying(IType parameterType, IType targetType, Map<String, IType> qualifyingTypes) {
		return TypeVisiter.visit(parameterType, targetType, (referType, eachType) -> {
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

	public IType populateByQualifying(IType type, Map<String, IType> qualifyingTypes, IType targetType) {
		// 先使用类型填充
		targetType = populate(type, targetType);
		// 再用限定类型填充
		targetType = populateByQualifying(qualifyingTypes, targetType);
		// 返回类型
		return targetType;
	}

	public IType populateByQualifying(Map<String, IType> qualifyingTypes, IType targetType) {
		return TypeVisiter.visit(targetType, eachType -> {
			if (eachType.isTypeVariable()) {
				return qualifyingTypes.get(targetType.getGenericName());
			}
			return eachType;
		});
	}
}
