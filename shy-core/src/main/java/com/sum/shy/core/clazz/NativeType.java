package com.sum.shy.core.clazz;

public class NativeType extends IType {

//	public Method findMethod(String methodName, List<IType> parameterTypes) {
//		// 这里要处理Object...这种形式
//		for (Method method : clazz.getMethods()) {
//			if (method.getName().equals(methodName) && method.getParameterCount() == parameterTypes.size()) {
//				boolean flag = true;
//				int count = 0;
////				for (Parameter parameter : method.getParameters()) {
////					NativeType nativeType = (NativeType) NativeVisiter.visitMember(super.clazz, this,
////							parameter.getParameterizedType());
////					IType type = parameterTypes.get(count++);
////					Class<?> clazz = ReflectUtils.getClass(type.getClassName());
////					if (!(clazz.isAssignableFrom(nativeType.clazz))) {
////						flag = false;
////						break;
////					}
////				}
//				if (flag)
//					return method;
//			}
//		}
//		for (Method method : clazz.getMethods()) {
//			if (method.getName().equals(methodName)) {
//				return method;
//			}
//		}
//		throw new RuntimeException("The method was not found!method:" + methodName);
//	}

}
