package com.sum.spirit.plug.test;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class VelocityTest {

	public static void main(String[] args) {
		test2();
	}

	private static void test2() {
		// 初始化模板引擎
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();
		// 获取模板文件
		Template t = ve.getTemplate("mapper.vm");
		// 设置变量
		VelocityContext ctx = new VelocityContext();
		ctx.put("daoClassName", "com.sum.testDto");
		ctx.put("voClassName", "com.sum.testVo");
		ctx.put("tableName", "user_info");
		List<FieldInfo> fieldInfos = new ArrayList<>();
		fieldInfos.add(new FieldInfo("id", "id", "#{id}", true /* primary */, false, false, false));
		fieldInfos.add(new FieldInfo("name", "name", "#{name}", false, false, false, false));
		fieldInfos.add(new FieldInfo("age", "age", "#{age}", false, false, false, false));
		fieldInfos.add(new FieldInfo("userName", "user_name", "#{userName}", false, true /* like */, false, false));
		fieldInfos.add(new FieldInfo("updateTime", "update_time", "#{updateTime}", false, false, true /* range */, false));
		fieldInfos.add(new FieldInfo("userType", "user_type", "#{userType}", false, false, false, true /* enums */));
		ctx.put("fields", fieldInfos);
		// 输出
		StringWriter sw = new StringWriter();
		t.merge(ctx, sw);
		System.out.println(sw.toString());
	}

	public static class FieldInfo {
		public String property;
		public String column;
		public String express;
		public boolean primary = false;
		public boolean like = false;
		public boolean range = false;
		public boolean enums = false;

		public FieldInfo(String property, String column, String express, boolean primary, boolean like, boolean range, boolean enums) {
			this.property = property;
			this.column = column;
			this.express = express;
			this.primary = primary;
			this.like = like;
			this.range = range;
			this.enums = enums;
		}

		public String getUpperCase() {
			return property.substring(0, 1).toUpperCase() + property.substring(1);
		}

		public String getProperty() {
			return property;
		}

		public void setProperty(String property) {
			this.property = property;
		}

		public String getColumn() {
			return column;
		}

		public void setColumn(String column) {
			this.column = column;
		}

		public String getExpress() {
			return express;
		}

		public void setExpress(String express) {
			this.express = express;
		}

		public boolean isPrimary() {
			return primary;
		}

		public void setPrimary(boolean primary) {
			this.primary = primary;
		}

		public boolean isLike() {
			return like;
		}

		public void setLike(boolean like) {
			this.like = like;
		}

		public boolean isRange() {
			return range;
		}

		public void setRange(boolean range) {
			this.range = range;
		}

		public boolean isEnums() {
			return enums;
		}

		public void setEnums(boolean enums) {
			this.enums = enums;
		}

	}

}
