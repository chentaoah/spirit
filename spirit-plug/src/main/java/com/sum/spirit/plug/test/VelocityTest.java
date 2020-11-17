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
//		Template t = ve.getTemplate("mapper.vm", "UTF-8");
		Template t = ve.getTemplate("sql.vm", "UTF-8");
//		Template t = ve.getTemplate("dao.vm", "UTF-8");
//		Template t = ve.getTemplate("markdown.vm", "UTF-8");
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
		fieldInfos.add(new FieldInfo("id", "Integer", false, "id", "NUMBER", 0, true /* primary */, true /* not null */, "", "主键", false, false));
		fieldInfos.add(new FieldInfo("name", "String", false, "name", "VARCHAR", 50, false, false, "", "名称", true /* like */, false));
		fieldInfos.add(new FieldInfo("age", "Integer", false, "age", "NUMBER", 0, false, false, "", "年龄", false, false));
		fieldInfos.add(new FieldInfo("userName", "String", false, "user_name", "VARCHAR", 50, false, false, "", "用户名称", true /* like */, false));
		fieldInfos.add(new FieldInfo("updateTime", "Date", false, "update_time", "DATETIME", 0, false, false, "", "更新时间", false, true /* range */));
		fieldInfos
				.add(new FieldInfo("userType", "Integer", true /* required */, "user_type", "ENUM", 0, false, true /* not null */, "0", "用户类型", false, false));
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
		public String property;// 属性名
		public String express;// 表达式
		public String javaType;// 类型。Integer, String, Date
		public boolean required = false;// 是否必须传值

		public String column;// 字段名
		public String jdbcType;// 类型。枚举ENUM，数字NUMBER，浮点数字DECIMAL，短字符串VARCHAR，长字符串TEXT，时间DATETIME
		public int length;// 长度
		public boolean primary = false;// 是否主键
		public boolean notNull = false;// 非空
		public String defaultValue; // 默认值
		public String comment; // 备注
		public boolean like = false; // 是否模糊查询
		public boolean range = false;// 是否范围查询

		public FieldInfo(String property, String javaType, boolean required, String column, String jdbcType, int length, boolean primary, boolean notNull,
				String defaultValue, String comment, boolean like, boolean range) {
			this.property = property;
			this.express = "#{" + property + "}";
			this.javaType = javaType;
			this.required = required;
			this.column = column;
			this.jdbcType = jdbcType;
			this.length = length;
			this.primary = primary;
			this.notNull = notNull;
			this.defaultValue = defaultValue;
			this.comment = comment;
			this.like = like;
			this.range = range;
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

		public String getExpress() {
			return express;
		}

		public void setExpress(String express) {
			this.express = express;
		}

		public String getJavaType() {
			return javaType;
		}

		public void setJavaType(String javaType) {
			this.javaType = javaType;
		}

		public boolean isRequired() {
			return required;
		}

		public void setRequired(boolean required) {
			this.required = required;
		}

		public String getColumn() {
			return column;
		}

		public void setColumn(String column) {
			this.column = column;
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

		public boolean isPrimary() {
			return primary;
		}

		public void setPrimary(boolean primary) {
			this.primary = primary;
		}

		public boolean isNotNull() {
			return notNull;
		}

		public void setNotNull(boolean notNull) {
			this.notNull = notNull;
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

	}

}
