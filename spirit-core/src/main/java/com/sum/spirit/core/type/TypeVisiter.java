package com.sum.spirit.core.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.utils.TypeBuilder;

public class TypeVisiter {

	public IType visit(IType targetType, SimpleAction action) {
		return visit(null, -1, null, targetType, action);
	}

	public IType visit(IType referenceType, IType targetType, ReferenceAction action) {
		return visit(null, -1, referenceType, targetType, action);
	}

	private IType visit(IType rawType, int index, IType referenceType, IType targetType, Action action) {
		// 校验
		if (targetType == null) {
			return targetType;
		}
		// 拷贝一份
		targetType = TypeBuilder.copy(targetType);
		// 触发一次
		IType returnType = null;
		if (action instanceof SimpleAction) {
			returnType = ((SimpleAction) action).execute(rawType, index, targetType);
		} else if (action instanceof ReferenceAction) {
			returnType = ((ReferenceAction) action).execute(rawType, index, referenceType, targetType);
		}
		if (returnType != null) {
			targetType = returnType;
		}
		// 如果是泛型，则向下触发
		if (targetType.isGenericType()) {
			// 注意，泛型集合是不可轻易修改的
			List<IType> genericTypes = new ArrayList<>(targetType.getGenericTypes());
			// 是否修改的标志
			boolean flag = false;
			for (int idx = 0; idx < genericTypes.size(); idx++) {
				returnType = visit(targetType, idx, referenceType != null ? referenceType.getGenericTypes().get(idx) : null, genericTypes.get(idx), action);
				if (returnType != null) {
					genericTypes.set(idx, returnType);
					flag = true;
				}
			}
			if (flag) {
				targetType.setGenericTypes(Collections.unmodifiableList(genericTypes));
			}
		}
		return targetType;
	}

	public static interface Action {
	}

	public static interface SimpleAction extends Action {
		IType execute(IType rawType, int index, IType targetType);
	}

	public static interface ReferenceAction extends Action {
		IType execute(IType rawType, int index, IType referenceType, IType targetType);
	}

}
