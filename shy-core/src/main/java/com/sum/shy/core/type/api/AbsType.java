package com.sum.shy.core.type.api;

public abstract class AbsType {

//	@Override
//	public boolean equals(Object obj) {
//		if (obj instanceof IType) {
//			IType type = (IType) obj;
//			boolean flag = getClassName().equals(type.getClassName());
//			if (flag) {
//				int count = 0;
//				for (IType genericType : getGenericTypes()) {
//					if (!genericType.equals(type.getGenericTypes().get(count++))) {
//						flag = false;
//						break;
//					}
//				}
//			}
//			return flag;
//		}
//		return false;
//	}

	@Override
	public String toString() {
		// 最终是否打印类全名，看是否能够添加到该类型中
//		String finalName = null;
//		if (isWildcard()) {
//			finalName = "?";
//		} else {
//			if (clazz.addImport(getClassName())) {
//				finalName = getSimpleName();
//			} else {
//				finalName = TypeUtils.removeDecoration(getClassName()) + (isArray() ? "[]" : "");
//			}
//		}
//		if (!isArray() && !isGenericType()) {// 普通类型
//			return finalName;
//		} else if (isArray() && !isGenericType()) {// 数组
//			return finalName;
//		} else if (!isArray() && isGenericType()) {// 泛型
//			return finalName + "<" + Joiner.on(", ").join(getGenericTypes()) + ">";
//		}
		return null;
	}
}
