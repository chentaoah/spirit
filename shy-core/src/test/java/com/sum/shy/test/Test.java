package com.sum.shy.test;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.analyzer.LexicalAnalyzer;

public class Test {

	public static void main(String[] args) {

		String text = "func main(){";

		List<String> list = LexicalAnalyzer.getWords(text);

		System.out.println(list);

//		try {
////			List<String> list=new ArrayList<String>();
////			
////			Method method = list.getClass().getMethod("get", int.class);
//			Method method = Child.class.getMethod("getName1");
//			Class<?> class1 = method.getReturnType();
//			// 获取泛型参数名
//			TypeVariable<?>[] params = class1.getTypeParameters(); // E
//
//			Type genericType = method.getGenericReturnType();
//			System.out.println(genericType);
//			if (genericType instanceof ParameterizedType) {
//				java.lang.reflect.Type[] actualTypeArguments = ((ParameterizedType) genericType)
//						.getActualTypeArguments();
//				for (java.lang.reflect.Type actualTypeArgument : actualTypeArguments) {
//					if (actualTypeArgument instanceof Class) {
//						System.out.println(actualTypeArgument);
//					}
//				}
//				System.out.println(((ParameterizedType) genericType).getOwnerType());
//				System.out.println(((ParameterizedType) genericType).getRawType());
//				System.out.println(((ParameterizedType) genericType).getTypeName());
//			}
//
//		} catch (NoSuchMethodException | SecurityException e) {
//			e.printStackTrace();
//		}

//		public CodeType(String type) {// 这里也不再支持多层嵌套的泛型了
		//
//				if ("bool".equals(type)) {
//					this.category = Constants.BOOL_TYPE;
//					this.name = "bool";
		//
//				} else if ("int".equals(type)) {
//					this.category = Constants.INT_TYPE;
//					this.name = "int";
		//
//				} else if ("long".equals(type)) {
//					this.category = Constants.LONG_TYPE;
//					this.name = "long";
		//
//				} else if ("double".equals(type)) {
//					this.category = Constants.DOUBLE_TYPE;
//					this.name = "double";
		//
//				} else if ("str".equals(type)) {
//					this.category = Constants.STR_TYPE;
//					this.name = "str";
		//
//				} else if ("obj".equals(type)) {
//					this.category = Constants.OBJ_TYPE;
//					this.name = "obj";
		//
//				} else if ("void".equals(type)) {
//					this.category = Constants.VOID_TYPE;
//					this.name = "void";
		//
//				} else if (TYPE_PATTERN.matcher(type).matches()) {
//					this.category = Constants.CLASS_TYPE;
//					this.name = type;
		//
//				} else if (ARRAY_TYPE_PATTERN.matcher(type).matches()) {// 这里废弃了java里的String[]类型
//					this.category = Constants.ARRAY_TYPE;
//					this.name = "array";
//					this.genericTypes.put("E", type.substring(0, type.indexOf("[")));
		//
//				} else if (GENERIC_TYPE_PATTERN.matcher(type).matches() && type.startsWith("map<")) {
//					this.category = Constants.MAP_TYPE;
//					this.name = "map";
//					this.genericTypes.put("K", type.substring(type.indexOf("<") + 1, type.indexOf(",")));
//					this.genericTypes.put("V", type.substring(type.indexOf(",") + 1, type.indexOf(">")));
		//
//				} else if (GENERIC_TYPE_PATTERN.matcher(type).matches()) {
//					this.category = Constants.GENERIC_CLASS_TYPE;
//					this.name = type.substring(0, type.indexOf("<"));
//					this.genericTypes.put("K", type.substring(type.indexOf("<") + 1, type.indexOf(",")));
//					this.genericTypes.put("V", type.substring(type.indexOf(",") + 1, type.indexOf(">")));
		//
//				} else {
//					throw new RuntimeException("Type not currently supported!name:" + type);
		//
//				}
		//
//			}

	}

}
