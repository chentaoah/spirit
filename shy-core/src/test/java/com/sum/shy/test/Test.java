package com.sum.shy.test;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.Child;
import com.sum.shy.core.People;

public class Test {

	public static void main(String[] args) {
		try {
//			List<String> list=new ArrayList<String>();
//			
//			Method method = list.getClass().getMethod("get", int.class);
			Method method = Child.class.getMethod("getName1");
			Class<?> class1 = method.getReturnType();
			// 获取泛型参数名
			TypeVariable<?>[] params = class1.getTypeParameters(); // E

			Type genericType = method.getGenericReturnType();
			System.out.println(genericType);
			if (genericType instanceof ParameterizedType) {
				java.lang.reflect.Type[] actualTypeArguments = ((ParameterizedType) genericType)
						.getActualTypeArguments();
				for (java.lang.reflect.Type actualTypeArgument : actualTypeArguments) {
					if (actualTypeArgument instanceof Class) {
						System.out.println(actualTypeArgument);
					}
				}
				System.out.println(((ParameterizedType) genericType).getOwnerType());
				System.out.println(((ParameterizedType) genericType).getRawType());
				System.out.println(((ParameterizedType) genericType).getTypeName());
			}

		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

}
