package com.gitee.spirit.core.compile.linker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Dictionary;
import com.gitee.spirit.core.api.ClassLinker;
import com.gitee.spirit.core.api.TypeFactory;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.clazz.utils.CommonTypes;

import cn.hutool.core.lang.Assert;

@Primary
@Component
public class AdaptiveClassLinker implements ClassLinker {

	@Autowired
	public PrimitiveClassLinker primitiveClassLinker;
	@Autowired
	public ArrayClassLinker arrayClassLinker;
	@Autowired
	public AppClassLinker appClassLinker;
	@Autowired
	@Qualifier("extClassLinker")
	public ClassLinker extClassLinker;
	@Autowired
	public TypeFactory factory;

	public ClassLinker chooseLinker(IType type) {
		if (type.isPrimitive()) {
			return primitiveClassLinker;
		} else if (type.isArray()) {
			return arrayClassLinker;
		} else if (!type.isNative()) {
			return appClassLinker;
		} else {
			return extClassLinker;
		}
	}

	@Override
	public <T> T toClass(IType type) {
		return chooseLinker(type).toClass(type);
	}

	@Override
	public int getTypeVariableIndex(IType type, String genericName) {
		return chooseLinker(type).getTypeVariableIndex(type, genericName);
	}

	@Override
	public IType getSuperType(IType type) {
		// Object已经没有父类了
		if (CommonTypes.OBJECT.equals(type)) {
			return null;
		}
		// 如果不存在显示的父类，则返回Object
		IType superType = chooseLinker(type).getSuperType(type);
		if (superType == null) {
			return type.isPrimitive() ? null : CommonTypes.OBJECT;
		}
		// 降低访问权限
		return superType.lowerAccessLevel();
	}

	@Override
	public List<IType> getInterfaceTypes(IType type) {
		return chooseLinker(type).getInterfaceTypes(type);
	}

	@Override
	public IType visitField(IType type, String fieldName) throws NoSuchFieldException {
		Assert.notNull(type, "Type cannot be null!");
		Assert.notEmpty(fieldName, "Field name cannot be empty!");
		// obj.class class是关键字
		if (Dictionary.CLASS.equals(fieldName)) {
			return factory.create(CommonTypes.CLASS.getClassName(), type.toBox());
		}
		IType returnType = chooseLinker(type).visitField(type, fieldName);
		if (returnType == null) {
			IType superType = getSuperType(type);
			if (superType != null) {
				return visitField(superType, fieldName);
			}
		}
		if (returnType == null) {
			throw new NoSuchFieldException(String.format("No such field!className:%s, fieldName:%s", type.getClassName(), fieldName));
		}
		return returnType;
	}

	@Override
	public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) throws NoSuchMethodException {
		Assert.notNull(type, "Type cannot be null!");
		Assert.notEmpty(methodName, "Method name cannot be empty!");
		// super()和this()指代父类或者本身的构造函数，返回这个类本身
		if (Dictionary.SUPER.equals(methodName) || Dictionary.THIS.equals(methodName)) {
			return type;
		}
		// 如果已经推导到Object，并且方法名是empty的话，则直接返回布尔类型
		if (CommonTypes.OBJECT.equals(type) && Dictionary.EMPTY.equals(methodName)) {
			return CommonTypes.BOOLEAN;
		}
		IType returnType = chooseLinker(type).visitMethod(type, methodName, parameterTypes);
		if (returnType == null) {
			IType superType = getSuperType(type);
			if (superType != null) {
				return visitMethod(superType, methodName, parameterTypes);
			}
		}
		if (returnType == null) {
			throw new NoSuchMethodException(String.format("No such method!className:%s, methodName:%s", type.getClassName(), methodName));
		}
		return returnType;
	}

}
