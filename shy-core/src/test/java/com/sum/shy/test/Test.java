package com.sum.shy.test;

public class Test {
	//
//	public static List<String> getGenericTypes(Map<String, String> defTypes, String type, Stmt stmt) {
//		List<String> genericTypes = new ArrayList<>();
//		if ("array".equals(type)) {
//			Stmt subSentence = stmt.getSubSentence(2);
//			String genericType = getType(defTypes, subSentence);
//			genericTypes.add(genericType);
//			return genericTypes;
//		} else if ("map".equals(type)) {
//			genericTypes.add("var");
//			genericTypes.add("var");
//			Stmt subSentence = stmt.getSubSentence(2);
//			boolean flag = true;
//			for (int j = 0; j < subSentence.words.size(); j++) {
//				String unit = subSentence.getUnit(j);
//				if (":".equals(unit)) {
//					flag = false;
//				} else if (",".equals(unit)) {
//					flag = true;
//				}
//				String genericType = Analyzer.getType(defTypes, unit);
//				if (!"var".equals(genericType)) {
//					genericTypes.set(flag ? 0 : 1, genericType);
//					if (!"var".equals(genericTypes.get(0)) && !"var".equals(genericTypes.get(1))) {
//						return genericTypes;
//					}
//				}
//			}
//		}
//
//		return genericTypes;
//
//	}
//
}
