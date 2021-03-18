package com.sum.spirit.core.lexer.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sum.spirit.common.enums.TokenTypeEnum;
import com.sum.spirit.core.api.ElementBuilder;
import com.sum.spirit.core.element.entity.Element;
import com.sum.spirit.core.element.entity.Token;

@SpringBootTest
@DisplayName("语法分析器测试")
public class ElementTest {

	@Autowired
	public ElementBuilder builder;

	public static void assertTypeAndValue(Token token, TokenTypeEnum tokenTypeEnum, String value) {
		assertEquals(token.tokenType, tokenTypeEnum);
		assertEquals(token.toString(), value);
	}

	@Test
	@DisplayName("引入类型")
	public void test0000() {
		String text = "import com.sum.test.Service";
		Element element = builder.build(text);
		assertTrue(element.isImport());
		assertTrue(element.size() == 2);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "import");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.PATH, "com.sum.test.Service");
	}

	@Test
	@DisplayName("注释")
	public void test0001() {
		String text = "@Service(name=\"test\")";
		Element element = builder.build(text);
		assertTrue(element.isAnnotation());
		assertTrue(element.size() == 1);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.ANNOTATION, "@Service(name=\"test\")");
	}

	@Test
	@DisplayName("声明接口")
	public void test0002() {
		String text = "interface Service {";
		Element element = builder.build(text);
		assertTrue(element.isInterface());
		assertTrue(element.size() == 3);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "interface");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.TYPE, "Service");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

	@Test
	@DisplayName("声明抽象类型")
	public void test0003() {
		String text = "abstract Service {";
		Element element = builder.build(text);
		assertTrue(element.isAbstract());
		assertTrue(element.size() == 3);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "abstract");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.TYPE, "Service");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

	@Test
	@DisplayName("声明类型")
	public void test0004() {
		String text = "class Service {";
		Element element = builder.build(text);
		assertTrue(element.isClass());
		assertTrue(element.size() == 3);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "class");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.TYPE, "Service");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

	@Test
	@DisplayName("声明字段")
	public void test0005() {
		String text = "field = \"test\"";
		Element element = builder.build(text);
		assertTrue(element.isAssign());
		assertTrue(element.size() == 3);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.VARIABLE, "field");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.OPERATOR, "=");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.STRING, "\"test\"");
	}

	@Test
	@DisplayName("声明类型字段")
	public void test0006() {
		String text = "String field = \"test\"";
		Element element = builder.build(text);
		assertTrue(element.isDeclareAssign());
		assertTrue(element.size() == 4);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.TYPE, "String");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.VARIABLE, "field");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.OPERATOR, "=");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.STRING, "\"test\"");
	}

	@Test
	@DisplayName("声明方法")
	public void test0007() {
		String text = "func test(String arg0){";
		Element element = builder.build(text);
		assertTrue(element.isFunc());
		assertTrue(element.size() == 3);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "func");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.LOCAL_METHOD, "test(String arg0)");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

}
