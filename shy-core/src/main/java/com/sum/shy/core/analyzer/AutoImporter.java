package com.sum.shy.core.analyzer;

import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Field;
import com.sum.shy.core.entity.Method;

/**
 * 自动引入器
 * 
 * @author chentao26275
 *
 */
public class AutoImporter {

	public static void autoImport(Clazz clazz) {

		for (Field field : clazz.staticFields) {
			addImport(clazz, field);
		}
		for (Field field : clazz.fields) {
			addImport(clazz, field);
		}
		for (Method method : clazz.staticMethods) {
			addImport(clazz, method);
		}
		for (Method method : clazz.methods) {
			addImport(clazz, method);
		}

	}

	private static void addImport(Clazz clazz, Field field) {
		// TODO Auto-generated method stub

	}

	private static void addImport(Clazz clazz, Method method) {
		// TODO Auto-generated method stub

	}

}
