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
//		Template t = ve.getTemplate("mapper.vm");
//		Template t = ve.getTemplate("sql.vm");
//		Template t = ve.getTemplate("dao.vm");
		Template t = ve.getTemplate("markdown.vm", "UTF-8");
		// 设置变量
		VelocityContext ctx = new VelocityContext();
		ctx.put("package", "com.sum.test");
		ctx.put("daoClassName", "com.sum.TestDto");
		ctx.put("daoSimpleName", "TestDto");
		ctx.put("voClassName", "com.sum.TestVo");
		ctx.put("voSimpleName", "TestVo");
		ctx.put("tableName", "user_info");
		ctx.put("tableComment", "用户信息表");
		List<FieldInfo> fieldInfos = new ArrayList<>();
		fieldInfos.add(new FieldInfo("Integer", "id", "id", "#{id}", "", 0, "", "主键", true /* primary */, false, false, false));
		fieldInfos.add(new FieldInfo("String", "name", "name", "#{name}", "VARCHAR", 50, "", "名称", false, false, false, false));
		fieldInfos.add(new FieldInfo("Integer", "age", "age", "#{age}", "NUMBER", 0, "", "年龄", false, false, false, false));
		fieldInfos.add(new FieldInfo("String", "userName", "user_name", "#{userName}", "TEXT", 50, "", "用户名称", false, true /* like */, false, false));
		fieldInfos.add(new FieldInfo("Date", "updateTime", "update_time", "#{updateTime}", "DATETIME", 0, "", "更新时间", false, false, true /* range */, false));
		fieldInfos.add(new FieldInfo("Integer", "userType", "user_type", "#{userType}", "ENUM", 0, "0", "用户类型", false, false, false, true /* required */));
		// 索引
		ctx.put("fields", fieldInfos);
		List<String> indexs = new ArrayList<>();
		indexs.add("'name', 'age', 'user_name'");
		ctx.put("indexs", indexs);
		// 输出
		StringWriter sw = new StringWriter();
		t.merge(ctx, sw);
		System.out.println(sw.toString());
	}

	public static class FieldInfo {
		public String javaType;// Integer, String, Date
		public String property;
		public String column;
		public String express;
		public String jdbcType;// 类型 枚举ENUM，数字NUMBER，浮点数字DECIMAL，短字符串VARCHAR，长字符串TEXT，时间DATETIME
		public int length;// 长度
		public String defaultValue;
		public String comment;
		public boolean primary = false;
		public boolean like = false;
		public boolean range = false;
		public boolean required = false;

		public FieldInfo(String javaType, String property, String column, String express, String jdbcType, int length, String defaultValue, String comment,
				boolean primary, boolean like, boolean range, boolean required) {
			this.javaType = javaType;
			this.property = property;
			this.column = column;
			this.express = express;
			this.jdbcType = jdbcType;
			this.length = length;
			this.defaultValue = defaultValue;
			this.comment = comment;
			this.primary = primary;
			this.like = like;
			this.range = range;
			this.required = required;
		}

		public String getUpperCase() {
			return property.substring(0, 1).toUpperCase() + property.substring(1);
		}

		public String getJavaType() {
			return javaType;
		}

		public void setJavaType(String javaType) {
			this.javaType = javaType;
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

		public String getJdbcType() {
			return jdbcType;
		}

		public void setJdbcType(String jdbcType) {
			this.jdbcType = jdbcType;
		}

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public String getDefaultValue() {
			return defaultValue;
		}

		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
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

		public boolean isRequired() {
			return required;
		}

		public void setRequired(boolean required) {
			this.required = required;
		}

	}

}
