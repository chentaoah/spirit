package com.sum.spirit.java.link;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassLoader;
import com.sum.spirit.core.type.TypeBuilder;
import com.sum.spirit.java.utils.ReflectUtils;
import com.sum.spirit.pojo.enums.TypeEnum;
import com.sum.spirit.utils.TypeUtils;

@Component
@Order(-100)
public class BootstrapClassLoader implements ClassLoader {

	@Override
	public void load() {
		TypeEnum.VOID_WRAPPED.value = TypeBuilder.build("java.lang.Void", "Void", "java.lang.Void", false, false, false, false, true);
		TypeEnum.BOOLEAN_WRAPPED.value = TypeBuilder.build("java.lang.Boolean", "Boolean", "java.lang.Boolean", false, false, false, false, true);
		TypeEnum.CHAR_WRAPPED.value = TypeBuilder.build("java.lang.Character", "Character", "java.lang.Character", false, false, false, false, true);
		TypeEnum.BYTE_WRAPPED.value = TypeBuilder.build("java.lang.Byte", "Byte", "java.lang.Byte", false, false, false, false, true);
		TypeEnum.SHORT_WRAPPED.value = TypeBuilder.build("java.lang.Short", "Short", "java.lang.Short", false, false, false, false, true);
		TypeEnum.INT_WRAPPED.value = TypeBuilder.build("java.lang.Integer", "Integer", "java.lang.Integer", false, false, false, false, true);
		TypeEnum.LONG_WRAPPED.value = TypeBuilder.build("java.lang.Long", "Long", "java.lang.Long", false, false, false, false, true);
		TypeEnum.FLOAT_WRAPPED.value = TypeBuilder.build("java.lang.Float", "Float", "java.lang.Float", false, false, false, false, true);
		TypeEnum.DOUBLE_WRAPPED.value = TypeBuilder.build("java.lang.Double", "Double", "java.lang.Double", false, false, false, false, true);

		TypeEnum.OBJECT.value = TypeBuilder.build("java.lang.Object", "Object", "java.lang.Object", false, false, false, false, true);
		TypeEnum.STRING.value = TypeBuilder.build("java.lang.String", "String", "java.lang.String", false, false, false, false, true);
		TypeEnum.OBJECT_ARRAY.value = TypeBuilder.build("[Ljava.lang.Object;", "Object[]", "java.lang.Object[]", false, true/* array */, false, false, true);
		TypeEnum.STRING_ARRAY.value = TypeBuilder.build("[Ljava.lang.String;", "String[]", "java.lang.String[]", false, true/* array */, false, false, true);

		TypeEnum.CLASS.value = TypeBuilder.build("java.lang.Class", "Class", "java.lang.Class", false, false, false, false, true);
		TypeEnum.LIST.value = TypeBuilder.build("java.util.List", "List", "java.util.List", false, false, false, false, true);
		TypeEnum.MAP.value = TypeBuilder.build("java.util.Map", "Map", "java.util.Map", false, false, false, false, true);

		TypeEnum.NULL.value = TypeBuilder.build("java.lang.Object", "Object", "java.lang.Object", false, false, true/* null */, false, true);
		TypeEnum.WILDCARD.value = TypeBuilder.build("java.lang.Object", "Object", "java.lang.Object", false, false, false, true/* wildcard */, true);
	}

	@Override
	public String getClassName(String simpleName) {
		return ReflectUtils.getClassName(TypeUtils.getTargetName(simpleName), TypeUtils.isArray(simpleName));
	}

	@Override
	public boolean isLoaded(String className) {
		return className.startsWith("java.lang.");
	}

	@Override
	public boolean shouldImport(String className) {
		return false;
	}

}
