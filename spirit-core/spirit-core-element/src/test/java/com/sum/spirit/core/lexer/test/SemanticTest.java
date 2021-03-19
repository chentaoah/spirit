package com.sum.spirit.core.lexer.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sum.spirit.core.api.SemanticParser;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;

@SpringBootTest
@DisplayName("语义测试")
public class SemanticTest {

	@Autowired
	public SemanticParser parser;

	@Test
	@DisplayName("PATH")
	public void test0000() {
		String text = "com.sum.spirit.example.Animal";
		Token token = parser.getToken(text);
		assertTrue(token.isPath());
	}

	@Test
	@DisplayName("ANNOTATION")
	public void test0001() {
		String text = "@Animal";
		Token token = parser.getToken(text);
		assertTrue(token.isAnnotation());
	}

	@Test
	@DisplayName("KEYWORD")
	public void test0002() {
		String text = "class";
		Token token = parser.getToken(text);
		assertTrue(token.isKeyword());
	}

	@Test
	@DisplayName("OPERATOR")
	public void test0003() {
		String text = "=";
		Token token = parser.getToken(text);
		assertTrue(token.isOperator());
	}

	@Test
	@DisplayName("SEPARATOR")
	public void test0004() {
		String text = "{";
		Token token = parser.getToken(text);
		assertTrue(token.isSeparator());
	}

	@Test
	@DisplayName("TYPE")
	public void test0005() {
		String text = "Horse";
		Token token = parser.getToken(text);
		assertTrue(token.isType());
	}

	@Test
	@DisplayName("ARRAY_INIT")
	public void test0006() {
		String text = "Horse[1]";
		Token token = parser.getToken(text);
		assertTrue(token.isArrayInit());
	}

	@Test
	@DisplayName("TYPE_INIT")
	public void test0007() {
		String text = "Horse()";
		Token token = parser.getToken(text);
		assertTrue(token.isTypeInit());
	}

	@Test
	@DisplayName("NULL")
	public void test0008() {
		String text = "null";
		Token token = parser.getToken(text);
		assertTrue(token.isNull());
	}

	@Test
	@DisplayName("BOOLEAN")
	public void test0009() {
		String text = "true";
		Token token = parser.getToken(text);
		assertTrue(token.isBoolean());
	}

	@Test
	@DisplayName("CHAR")
	public void test0010() {
		String text = "'c'";
		Token token = parser.getToken(text);
		assertTrue(token.isChar());
	}

	@Test
	@DisplayName("INT")
	public void test0011() {
		String text = "0";
		Token token = parser.getToken(text);
		assertTrue(token.isInt());
	}

	@Test
	@DisplayName("LONG")
	public void test0012() {
		String text = "0L";
		Token token = parser.getToken(text);
		assertTrue(token.isLong());
	}

	@Test
	@DisplayName("DOUBLE")
	public void test0013() {
		String text = "0.0";
		Token token = parser.getToken(text);
		assertTrue(token.isDouble());
	}

	@Test
	@DisplayName("STRING")
	public void test0014() {
		String text = "\"Jessie\"";
		Token token = parser.getToken(text);
		assertTrue(token.isString());
	}

	@Test
	@DisplayName("LIST")
	public void test0015() {
		String text = "[\"Jessie\"]";
		Token token = parser.getToken(text);
		assertTrue(token.isList());
	}

	@Test
	@DisplayName("MAP")
	public void test0016() {
		String text = "{\"Jessie\" : 0}";
		Token token = parser.getToken(text);
		assertTrue(token.isMap());
	}

	@Test
	@DisplayName("SUBEXPRESS")
	public void test0017() {
		String text = "(x + y)";
		Token token = parser.getToken(text);
		assertTrue(token.isSubexpress());
	}

	@Test
	@DisplayName("CAST")
	public void test0018() {
		String text = "(Horse)";
		Token token = parser.getToken(text);
		assertTrue(token.isCast());
	}

	@Test
	@DisplayName("VARIABLE")
	public void test0019() {
		String text = "name";
		Token token = parser.getToken(text);
		assertTrue(token.isVariable());
	}

	@Test
	@DisplayName("LOCAL_METHOD")
	public void test0020() {
		String text = "call()";
		Token token = parser.getToken(text);
		assertTrue(token.isLocalMethod());
	}

	@Test
	@DisplayName("VISIT_FIELD")
	public void test0021() {
		String text = ".name";
		Token token = parser.getToken(text);
		assertTrue(token.isVisitField());
	}

	@Test
	@DisplayName("VISIT_METHOD")
	public void test0022() {
		String text = ".call()";
		Token token = parser.getToken(text);
		assertTrue(token.isVisitMethod());
	}

	@Test
	@DisplayName("VISIT_INDEX")
	public void test0023() {
		String text = "[0]";
		Token token = parser.getToken(text);
		assertTrue(token.isVisitIndex());
	}

	@Test
	@DisplayName("PREFIX")
	public void test0024() {
		String text = ".call()";
		Token token = parser.getToken(text);
		Statement statement = token.getValue();
		assertTrue(statement.get(0).isPrefix());
	}

}
