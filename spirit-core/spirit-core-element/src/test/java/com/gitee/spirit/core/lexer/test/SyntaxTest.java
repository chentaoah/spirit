package com.gitee.spirit.core.lexer.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gitee.spirit.common.enums.TokenTypeEnum;
import com.gitee.spirit.core.api.ElementBuilder;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.entity.Token;

@SpringBootTest
@DisplayName("语法测试")
public class SyntaxTest {

	@Autowired
	public ElementBuilder builder;

	public static void assertTypeAndValue(Token token, TokenTypeEnum tokenTypeEnum, String value) {
		assertEquals(token.tokenType, tokenTypeEnum);
		assertEquals(token.toString(), value);
	}

	@Test
	@DisplayName("IMPORT")
	public void test0000() {
		String text = "import com.gitee.spirit.example.Animal";
		Element element = builder.build(text);
		assertTrue(element.isImport());
		assertTrue(element.size() == 2);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "import");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.ACCESS_PATH, "com.gitee.spirit.example.Animal");
	}

	@Test
	@DisplayName("ANNOTATION")
	public void test0001() {
		String text = "@Animal";
		Element element = builder.build(text);
		assertTrue(element.isAnnotation());
		assertTrue(element.size() == 1);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.ANNOTATION, "@Animal");
	}

	@Test
	@DisplayName("INTERFACE")
	public void test0002() {
		String text = "interface Horse {";
		Element element = builder.build(text);
		assertTrue(element.isInterface());
		assertTrue(element.size() == 3);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "interface");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.TYPE, "Horse");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

	@Test
	@DisplayName("ABSTRACT")
	public void test0003() {
		String text = "abstract Horse {";
		Element element = builder.build(text);
		assertTrue(element.isAbstract());
		assertTrue(element.size() == 3);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "abstract");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.TYPE, "Horse");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

	@Test
	@DisplayName("CLASS")
	public void test0004() {
		String text = "class Horse {";
		Element element = builder.build(text);
		assertTrue(element.isClass());
		assertTrue(element.size() == 3);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "class");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.TYPE, "Horse");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

	@Test
	@DisplayName("SUPER")
	public void test0005() {
		String text = "super()";
		Element element = builder.build(text);
		assertTrue(element.isSuper());
		assertTrue(element.size() == 1);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.LOCAL_METHOD, "super()");
	}

	@Test
	@DisplayName("THIS")
	public void test0006() {
		String text = "this()";
		Element element = builder.build(text);
		assertTrue(element.isThis());
		assertTrue(element.size() == 1);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.LOCAL_METHOD, "this()");
	}

	@Test
	@DisplayName("DECLARE")
	public void test0007() {
		String text = "String name";
		Element element = builder.build(text);
		assertTrue(element.isDeclare());
		assertTrue(element.size() == 2);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.TYPE, "String");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.VARIABLE, "name");
	}

	@Test
	@DisplayName("DECLARE_ASSIGN")
	public void test0008() {
		String text = "String name = \"Jessie\"";
		Element element = builder.build(text);
		assertTrue(element.isDeclareAssign());
		assertTrue(element.size() == 4);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.TYPE, "String");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.VARIABLE, "name");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.OPERATOR, "=");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.STRING, "\"Jessie\"");
	}

	@Test
	@DisplayName("ASSIGN")
	public void test0009() {
		String text = "name = \"Jessie\"";
		Element element = builder.build(text);
		assertTrue(element.isAssign());
		assertTrue(element.size() == 3);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.VARIABLE, "name");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.OPERATOR, "=");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.STRING, "\"Jessie\"");
	}

	@Test
	@DisplayName("DECLARE_FUNC")
	public void test0010() {
		String text = "String call() throws Exception {";
		Element element = builder.build(text);
		assertTrue(element.isDeclareFunc());
		assertTrue(element.size() == 5);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.TYPE, "String");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.LOCAL_METHOD, "call()");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "throws");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.TYPE, "Exception");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

	@Test
	@DisplayName("FUNC")
	public void test0011() {
		String text = "func call() {";
		Element element = builder.build(text);
		assertTrue(element.isFunc());
		assertTrue(element.size() == 3);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "func");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.LOCAL_METHOD, "call()");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

	@Test
	@DisplayName("FIELD_ASSIGN")
	public void test0012() {
		String text = "horse.name = \"Jessie\"";
		Element element = builder.build(text);
		assertTrue(element.isFieldAssign());
		assertTrue(element.size() == 4);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.VARIABLE, "horse");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.VISIT_FIELD, ".name");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.OPERATOR, "=");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.STRING, "\"Jessie\"");
	}

	@Test
	@DisplayName("INVOKE")
	public void test0013() {
		String text = "horse.call()";
		Element element = builder.build(text);
		assertTrue(element.isInvoke());
		assertTrue(element.size() == 2);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.VARIABLE, "horse");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.VISIT_METHOD, ".call()");
	}

	@Test
	@DisplayName("RETURN")
	public void test0014() {
		String text = "return \"I am Jessie!\"";
		Element element = builder.build(text);
		assertTrue(element.isReturn());
		assertTrue(element.size() == 2);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "return");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.STRING, "\"I am Jessie!\"");
	}

	@Test
	@DisplayName("IF")
	public void test0015() {
		String text = "if name == \"Jessie\" {";
		Element element = builder.build(text);
		assertTrue(element.isIf());
		assertTrue(element.size() == 5);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "if");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.VARIABLE, "name");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.OPERATOR, "==");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.STRING, "\"Jessie\"");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

	@Test
	@DisplayName("ELSE_IF")
	public void test0016() {
		String text = "} else if name == \"Jessie\" {";
		Element element = builder.build(text);
		assertTrue(element.isElseIf());
		assertTrue(element.size() == 7);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "}");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "else");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "if");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.VARIABLE, "name");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.OPERATOR, "==");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.STRING, "\"Jessie\"");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

	@Test
	@DisplayName("ELSE")
	public void test0017() {
		String text = "} else {";
		Element element = builder.build(text);
		assertTrue(element.isElse());
		assertTrue(element.size() == 3);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "}");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "else");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

	@Test
	@DisplayName("END")
	public void test0018() {
		String text = "}";
		Element element = builder.build(text);
		assertTrue(element.isEnd());
		assertTrue(element.size() == 1);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "}");
	}

	@Test
	@DisplayName("FOR")
	public void test0019() {
		String text = "for (i=0; i<100; i++) {";
		Element element = builder.build(text);
		assertTrue(element.isFor());
		assertTrue(element.size() == 3);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "for");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SUBEXPRESS, "(i = 0; i < 100; i++)");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

	@Test
	@DisplayName("FOR_IN")
	public void test0020() {
		String text = "for item in list {";
		Element element = builder.build(text);
		assertTrue(element.isForIn());
		assertTrue(element.size() == 5);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "for");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.VARIABLE, "item");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "in");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.VARIABLE, "list");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

	@Test
	@DisplayName("WHILE")
	public void test0021() {
		String text = "while name == \"Jessie\" {";
		Element element = builder.build(text);
		assertTrue(element.isWhile());
		assertTrue(element.size() == 5);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "while");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.VARIABLE, "name");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.OPERATOR, "==");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.STRING, "\"Jessie\"");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

	@Test
	@DisplayName("CONTINUE")
	public void test0022() {
		String text = "continue";
		Element element = builder.build(text);
		assertTrue(element.isContinue());
		assertTrue(element.size() == 1);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "continue");
	}

	@Test
	@DisplayName("BREAK")
	public void test0023() {
		String text = "break";
		Element element = builder.build(text);
		assertTrue(element.isBreak());
		assertTrue(element.size() == 1);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "break");
	}

	@Test
	@DisplayName("TRY")
	public void test0024() {
		String text = "try {";
		Element element = builder.build(text);
		assertTrue(element.isTry());
		assertTrue(element.size() == 2);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "try");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

	@Test
	@DisplayName("CATCH")
	public void test0025() {
		String text = "} catch Exception e {";
		Element element = builder.build(text);
		assertTrue(element.isCatch());
		assertTrue(element.size() == 5);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "}");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "catch");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.TYPE, "Exception");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.VARIABLE, "e");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

	@Test
	@DisplayName("FINALLY")
	public void test0026() {
		String text = "} finally {";
		Element element = builder.build(text);
		assertTrue(element.isFinally());
		assertTrue(element.size() == 3);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "}");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "finally");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

	@Test
	@DisplayName("THROW")
	public void test0027() {
		String text = "throw RuntimeException(e)";
		Element element = builder.build(text);
		assertTrue(element.isThrow());
		assertTrue(element.size() == 2);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "throw");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.TYPE_INIT, "RuntimeException(e)");
	}

	@Test
	@DisplayName("SYNC")
	public void test0028() {
		String text = "sync horse {";
		Element element = builder.build(text);
		assertTrue(element.isSync());
		assertTrue(element.size() == 3);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "sync");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.VARIABLE, "horse");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.SEPARATOR, "{");
	}

	@Test
	@DisplayName("PRINT")
	public void test0029() {
		String text = "print \"Jessie\"";
		Element element = builder.build(text);
		assertTrue(element.isPrint());
		assertTrue(element.size() == 2);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "print");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.STRING, "\"Jessie\"");
	}

	@Test
	@DisplayName("DEBUG")
	public void test0030() {
		String text = "debug \"Jessie\"";
		Element element = builder.build(text);
		assertTrue(element.isDebug());
		assertTrue(element.size() == 2);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "debug");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.STRING, "\"Jessie\"");
	}

	@Test
	@DisplayName("ERROR")
	public void test0031() {
		String text = "error \"Jessie\"";
		Element element = builder.build(text);
		assertTrue(element.isError());
		assertTrue(element.size() == 2);
		int count = 0;
		assertTypeAndValue(element.get(count++), TokenTypeEnum.KEYWORD, "error");
		assertTypeAndValue(element.get(count++), TokenTypeEnum.STRING, "\"Jessie\"");
	}

}
