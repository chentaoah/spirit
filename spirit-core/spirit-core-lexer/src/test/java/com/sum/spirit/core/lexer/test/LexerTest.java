package com.sum.spirit.core.lexer.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sum.spirit.core.api.Lexer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@DisplayName("词法测试")
public class LexerTest {

	@Autowired
	public Lexer lexer;

	@Test
	@DisplayName("集合List")
	public void test0000() {
		String text = "list = [\"name0\",\"name1\",\"name2\"]";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 3);
		int count = 0;
		assertEquals(words.get(count++), "list");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "[\"name0\",\"name1\",\"name2\"]");
	}

	@Test
	@DisplayName("集合List内部拆分")
	public void test0001() {
		String text = "[\"name0\",\"name1\",\"name2\"]";
		List<String> words = lexer.getSubWords(text, '(', ')', '[', ']', '{', '}');
		log.info(words.toString());
		assertTrue(words.size() == 7);
		int count = 0;
		assertEquals(words.get(count++), "[");
		assertEquals(words.get(count++), "\"name0\"");
		assertEquals(words.get(count++), ",");
		assertEquals(words.get(count++), "\"name1\"");
		assertEquals(words.get(count++), ",");
		assertEquals(words.get(count++), "\"name2\"");
		assertEquals(words.get(count++), "]");
	}

	@Test
	@DisplayName("集合Map")
	public void test0002() {
		String text = "map = {\"name\":\"tao.chen\",\"age\":29}";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 3);
		int count = 0;
		assertEquals(words.get(count++), "map");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "{\"name\":\"tao.chen\",\"age\":29}");
	}

	@Test
	@DisplayName("集合Map内部拆分")
	public void test0003() {
		String text = "{\"name\":\"tao.chen\",\"age\":29}";
		List<String> words = lexer.getSubWords(text, '(', ')', '[', ']', '{', '}');
		log.info(words.toString());
		assertTrue(words.size() == 9);
		int count = 0;
		assertEquals(words.get(count++), "{");
		assertEquals(words.get(count++), "\"name\"");
		assertEquals(words.get(count++), ":");
		assertEquals(words.get(count++), "\"tao.chen\"");
		assertEquals(words.get(count++), ",");
		assertEquals(words.get(count++), "\"age\"");
		assertEquals(words.get(count++), ":");
		assertEquals(words.get(count++), "29");
		assertEquals(words.get(count++), "}");
	}

	@Test
	@DisplayName("泛型")
	public void test0004() {
		String text = "map = HashMap<String,Object>()";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 3);
		int count = 0;
		assertEquals(words.get(count++), "map");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "HashMap<String,Object>()");
	}

	@Test
	@DisplayName("泛型初始化内部拆分")
	public void test0005() {
		String text = "HashMap<String,Object>()";
		List<String> words = lexer.getSubWords(text, '(', ')', '[', ']', '{', '}');
		log.info(words.toString());
		assertTrue(words.size() == 3);
		int count = 0;
		assertEquals(words.get(count++), "HashMap<String,Object>");
		assertEquals(words.get(count++), "(");
		assertEquals(words.get(count++), ")");
	}

	@Test
	@DisplayName("泛型类型内部拆分")
	public void test0006() {
		String text = "HashMap<String,Object>";
		List<String> words = lexer.getSubWords(text, '<', '>');
		log.info(words.toString());
		assertTrue(words.size() == 6);
		int count = 0;
		assertEquals(words.get(count++), "HashMap");
		assertEquals(words.get(count++), "<");
		assertEquals(words.get(count++), "String");
		assertEquals(words.get(count++), ",");
		assertEquals(words.get(count++), "Object");
		assertEquals(words.get(count++), ">");
	}

	@Test
	@DisplayName("数组")
	public void test0007() {
		String text = "array = String[]{\"name0\",\"name1\",\"name2\"}";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 3);
		int count = 0;
		assertEquals(words.get(count++), "array");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "String[]{\"name0\",\"name1\",\"name2\"}");
	}

	@Test
	@DisplayName("数组内部拆分")
	public void test0008() {
		String text = "String[]{\"name0\",\"name1\",\"name2\"}";
		List<String> words = lexer.getSubWords(text, '(', ')', '[', ']', '{', '}');
		log.info(words.toString());
		assertTrue(words.size() == 10);
		int count = 0;
		assertEquals(words.get(count++), "String");
		assertEquals(words.get(count++), "[");
		assertEquals(words.get(count++), "]");
		assertEquals(words.get(count++), "{");
		assertEquals(words.get(count++), "\"name0\"");
		assertEquals(words.get(count++), ",");
		assertEquals(words.get(count++), "\"name1\"");
		assertEquals(words.get(count++), ",");
		assertEquals(words.get(count++), "\"name2\"");
		assertEquals(words.get(count++), "}");
	}

	@Test
	@DisplayName("关键字")
	public void test0009() {
		String text = "class ServiceImpl<T,K> extends AbsService impls Service{";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 7);
		int count = 0;
		assertEquals(words.get(count++), "class");
		assertEquals(words.get(count++), "ServiceImpl<T,K>");
		assertEquals(words.get(count++), "extends");
		assertEquals(words.get(count++), "AbsService");
		assertEquals(words.get(count++), "impls");
		assertEquals(words.get(count++), "Service");
		assertEquals(words.get(count++), "{");
	}

	@Test
	@DisplayName("方法")
	public void test0010() {
		String text = "func getAlias(@Deprecated String name,int age){";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 3);
		int count = 0;
		assertEquals(words.get(count++), "func");
		assertEquals(words.get(count++), "getAlias(@Deprecated String name,int age)");
		assertEquals(words.get(count++), "{");
	}

	@Test
	@DisplayName("方法内部拆分")
	public void test0011() {
		String text = "getAlias(@Deprecated String name,int age)";
		List<String> words = lexer.getSubWords(text, '(', ')', '[', ']', '{', '}');
		log.info(words.toString());
		assertTrue(words.size() == 9);
		int count = 0;
		assertEquals(words.get(count++), "getAlias");
		assertEquals(words.get(count++), "(");
		assertEquals(words.get(count++), "@Deprecated");
		assertEquals(words.get(count++), "String");
		assertEquals(words.get(count++), "name");
		assertEquals(words.get(count++), ",");
		assertEquals(words.get(count++), "int");
		assertEquals(words.get(count++), "age");
		assertEquals(words.get(count++), ")");
	}

	@Test
	@DisplayName("复杂嵌套")
	public void test0012() {
		String text = "list = [{},{}]";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 3);
		int count = 0;
		assertEquals(words.get(count++), "list");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "[{},{}]");
	}

	@Test
	@DisplayName("复杂嵌套内部拆分")
	public void test0013() {
		String text = "[{},{}]";
		List<String> words = lexer.getSubWords(text, '(', ')', '[', ']', '{', '}');
		log.info(words.toString());
		assertTrue(words.size() == 5);
		int count = 0;
		assertEquals(words.get(count++), "[");
		assertEquals(words.get(count++), "{}");
		assertEquals(words.get(count++), ",");
		assertEquals(words.get(count++), "{}");
		assertEquals(words.get(count++), "]");
	}

	@Test
	@DisplayName("访问字段")
	public void test0014() {
		String text = "fatherName = people.father.name";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 5);
		int count = 0;
		assertEquals(words.get(count++), "fatherName");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "people");
		assertEquals(words.get(count++), ".father");
		assertEquals(words.get(count++), ".name");
	}

	@Test
	@DisplayName("访问方法")
	public void test0015() {
		String text = "fatherName = people.father().name()";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 5);
		int count = 0;
		assertEquals(words.get(count++), "fatherName");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "people");
		assertEquals(words.get(count++), ".father()");
		assertEquals(words.get(count++), ".name()");
	}

	@Test
	@DisplayName("访问字段数组索引")
	public void test0016() {
		String text = "fatherName = people.father().name[0]";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 6);
		int count = 0;
		assertEquals(words.get(count++), "fatherName");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "people");
		assertEquals(words.get(count++), ".father()");
		assertEquals(words.get(count++), ".name");
		assertEquals(words.get(count++), "[0]");
	}

	@Test
	@DisplayName("访问方法数组索引")
	public void test0017() {
		String text = "fatherName = people.father().name()[0]";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 6);
		int count = 0;
		assertEquals(words.get(count++), "fatherName");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "people");
		assertEquals(words.get(count++), ".father()");
		assertEquals(words.get(count++), ".name()");
		assertEquals(words.get(count++), "[0]");
	}

	@Test
	@DisplayName("逻辑操作符")
	public void test0018() {
		String text = "flag = !(x+1>0 && y<100) && s==\"test\" || s instanceof Object";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 12);
		int count = 0;
		assertEquals(words.get(count++), "flag");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "!");
		assertEquals(words.get(count++), "(x+1>0 && y<100)");
		assertEquals(words.get(count++), "&&");
		assertEquals(words.get(count++), "s");
		assertEquals(words.get(count++), "==");
		assertEquals(words.get(count++), "\"test\"");
		assertEquals(words.get(count++), "||");
		assertEquals(words.get(count++), "s");
		assertEquals(words.get(count++), "instanceof");
		assertEquals(words.get(count++), "Object");
	}

	@Test
	@DisplayName("计算操作符")
	public void test0019() {
		String text = "number = x%2*100/2 + y - z";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 13);
		int count = 0;
		assertEquals(words.get(count++), "number");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "x");
		assertEquals(words.get(count++), "%");
		assertEquals(words.get(count++), "2");
		assertEquals(words.get(count++), "*");
		assertEquals(words.get(count++), "100");
		assertEquals(words.get(count++), "/");
		assertEquals(words.get(count++), "2");
		assertEquals(words.get(count++), "+");
		assertEquals(words.get(count++), "y");
		assertEquals(words.get(count++), "-");
		assertEquals(words.get(count++), "z");
	}

}
