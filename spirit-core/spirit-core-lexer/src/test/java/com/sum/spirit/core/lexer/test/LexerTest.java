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
	@DisplayName("STRING")
	public void test0000() {
		String text = "name = \"Jessie\"";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 3);
		int count = 0;
		assertEquals(words.get(count++), "name");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "\"Jessie\"");
	}

	@Test
	@DisplayName("CHAR")
	public void test0001() {
		String text = "ch = 'c'";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 3);
		int count = 0;
		assertEquals(words.get(count++), "ch");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "'c'");
	}

	@Test
	@DisplayName("MAP")
	public void test0002() {
		String text = "map = {\"name\" : \"Jessie\"}";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 3);
		int count = 0;
		assertEquals(words.get(count++), "map");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "{\"name\" : \"Jessie\"}");
	}

	@Test
	@DisplayName("MAP_SUBWORDS")
	public void test0003() {
		String text = "{\"name\" : \"Jessie\"}";
		List<String> words = lexer.getSubWords(text, '(', ')', '[', ']', '{', '}');
		log.info(words.toString());
		assertTrue(words.size() == 5);
		int count = 0;
		assertEquals(words.get(count++), "{");
		assertEquals(words.get(count++), "\"name\"");
		assertEquals(words.get(count++), ":");
		assertEquals(words.get(count++), "\"Jessie\"");
		assertEquals(words.get(count++), "}");
	}

	@Test
	@DisplayName("MAP_COMPLEX_NESTING")
	public void test0004() {
		String text = "{\"horse\" : {\"name\" : \"Jessie\"}}";
		List<String> words = lexer.getSubWords(text, '(', ')', '[', ']', '{', '}');
		log.info(words.toString());
		assertTrue(words.size() == 5);
		int count = 0;
		assertEquals(words.get(count++), "{");
		assertEquals(words.get(count++), "\"horse\"");
		assertEquals(words.get(count++), ":");
		assertEquals(words.get(count++), "{\"name\" : \"Jessie\"}");
		assertEquals(words.get(count++), "}");
	}

	@Test
	@DisplayName("METHOD")
	public void test0005() {
		String text = "func call() {";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 3);
		int count = 0;
		assertEquals(words.get(count++), "func");
		assertEquals(words.get(count++), "call()");
		assertEquals(words.get(count++), "{");
	}

	@Test
	@DisplayName("METHOD_SUBWORDS")
	public void test0006() {
		String text = "call(name, age)";
		List<String> words = lexer.getSubWords(text, '(', ')', '[', ']', '{', '}');
		log.info(words.toString());
		assertTrue(words.size() == 6);
		int count = 0;
		assertEquals(words.get(count++), "call");
		assertEquals(words.get(count++), "(");
		assertEquals(words.get(count++), "name");
		assertEquals(words.get(count++), ",");
		assertEquals(words.get(count++), "age");
		assertEquals(words.get(count++), ")");
	}

	@Test
	@DisplayName("LIST")
	public void test0007() {
		String text = "horses = [horse0, horse1, horse2]";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 3);
		int count = 0;
		assertEquals(words.get(count++), "horses");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "[horse0, horse1, horse2]");
	}

	@Test
	@DisplayName("LIST_SUBWORDS")
	public void test0008() {
		String text = "[horse0, horse1, horse2]";
		List<String> words = lexer.getSubWords(text, '(', ')', '[', ']', '{', '}');
		log.info(words.toString());
		assertTrue(words.size() == 7);
		int count = 0;
		assertEquals(words.get(count++), "[");
		assertEquals(words.get(count++), "horse0");
		assertEquals(words.get(count++), ",");
		assertEquals(words.get(count++), "horse1");
		assertEquals(words.get(count++), ",");
		assertEquals(words.get(count++), "horse2");
		assertEquals(words.get(count++), "]");
	}

	@Test
	@DisplayName("LIST_COMPLEX_NESTING")
	public void test0009() {
		String text = "[[horse0], horse1, horse2]";
		List<String> words = lexer.getSubWords(text, '(', ')', '[', ']', '{', '}');
		log.info(words.toString());
		assertTrue(words.size() == 7);
		int count = 0;
		assertEquals(words.get(count++), "[");
		assertEquals(words.get(count++), "[horse0]");
		assertEquals(words.get(count++), ",");
		assertEquals(words.get(count++), "horse1");
		assertEquals(words.get(count++), ",");
		assertEquals(words.get(count++), "horse2");
		assertEquals(words.get(count++), "]");
	}

	@Test
	@DisplayName("ARRAY_SIZE_INIT")
	public void test0010() {
		String text = "horses = Horse[1]";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 3);
		int count = 0;
		assertEquals(words.get(count++), "horses");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "Horse[1]");
	}

	@Test
	@DisplayName("ARRAY_SIZE_INIT_SUBWORDS")
	public void test0011() {
		String text = "Horse[1]";
		List<String> words = lexer.getSubWords(text, '(', ')', '[', ']', '{', '}');
		log.info(words.toString());
		assertTrue(words.size() == 4);
		int count = 0;
		assertEquals(words.get(count++), "Horse");
		assertEquals(words.get(count++), "[");
		assertEquals(words.get(count++), "1");
		assertEquals(words.get(count++), "]");
	}

	@Test
	@DisplayName("ARRAY_LITERAL_INIT")
	public void test0012() {
		String text = "horses = Horse[]{horse}";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 3);
		int count = 0;
		assertEquals(words.get(count++), "horses");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "Horse[]{horse}");
	}

	@Test
	@DisplayName("ARRAY_LITERAL_INIT_SUBWORDS")
	public void test0013() {
		String text = "Horse[]{horse}";
		List<String> words = lexer.getSubWords(text, '(', ')', '[', ']', '{', '}');
		log.info(words.toString());
		assertTrue(words.size() == 6);
		int count = 0;
		assertEquals(words.get(count++), "Horse");
		assertEquals(words.get(count++), "[");
		assertEquals(words.get(count++), "]");
		assertEquals(words.get(count++), "{");
		assertEquals(words.get(count++), "horse");
		assertEquals(words.get(count++), "}");
	}

	@Test
	@DisplayName("ARRAY_VISIT_INDEX")
	public void test0014() {
		String text = "horse = horses[0]";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 4);
		int count = 0;
		assertEquals(words.get(count++), "horse");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "horses");
		assertEquals(words.get(count++), "[0]");
	}

	@Test
	@DisplayName("ARRAY_VISIT_INDEX_SUBWORDS")
	public void test0015() {
		String text = "horses[0]";
		List<String> words = lexer.getSubWords(text, '(', ')', '[', ']', '{', '}');
		log.info(words.toString());
		assertTrue(words.size() == 4);
		int count = 0;
		assertEquals(words.get(count++), "horses");
		assertEquals(words.get(count++), "[");
		assertEquals(words.get(count++), "0");
		assertEquals(words.get(count++), "]");
	}

	@Test
	@DisplayName("GENERIC_TYPE")
	public void test0016() {
		String text = "class Horse<T, K> {";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 3);
		int count = 0;
		assertEquals(words.get(count++), "class");
		assertEquals(words.get(count++), "Horse<T, K>");
		assertEquals(words.get(count++), "{");
	}

	@Test
	@DisplayName("GENERIC_TYPE_SUBWORDS")
	public void test0017() {
		String text = "Horse<T, K>";
		List<String> words = lexer.getSubWords(text, '<', '>');
		log.info(words.toString());
		assertTrue(words.size() == 6);
		int count = 0;
		assertEquals(words.get(count++), "Horse");
		assertEquals(words.get(count++), "<");
		assertEquals(words.get(count++), "T");
		assertEquals(words.get(count++), ",");
		assertEquals(words.get(count++), "K");
		assertEquals(words.get(count++), ">");
	}

	@Test
	@DisplayName("GENERIC_TYPE_COMPLEX_NESTING")
	public void test0018() {
		String text = "Horse<Horse<String, Object>, Object>";
		List<String> words = lexer.getSubWords(text, '<', '>');
		log.info(words.toString());
		assertTrue(words.size() == 6);
		int count = 0;
		assertEquals(words.get(count++), "Horse");
		assertEquals(words.get(count++), "<");
		assertEquals(words.get(count++), "Horse<String, Object>");
		assertEquals(words.get(count++), ",");
		assertEquals(words.get(count++), "Object");
		assertEquals(words.get(count++), ">");
	}

	@Test
	@DisplayName("GENERIC_TYPE_INIT")
	public void test0019() {
		String text = "horse = Horse<String, Object>()";
		List<String> words = lexer.getWords(text);
		log.info(words.toString());
		assertTrue(words.size() == 3);
		int count = 0;
		assertEquals(words.get(count++), "horse");
		assertEquals(words.get(count++), "=");
		assertEquals(words.get(count++), "Horse<String, Object>()");
	}

	@Test
	@DisplayName("GENERIC_TYPE_INIT_SUBWORDS")
	public void test0020() {
		String text = "Horse<String, Object>()";
		List<String> words = lexer.getSubWords(text, '(', ')', '[', ']', '{', '}');
		log.info(words.toString());
		assertTrue(words.size() == 3);
		int count = 0;
		assertEquals(words.get(count++), "Horse<String, Object>");
		assertEquals(words.get(count++), "(");
		assertEquals(words.get(count++), ")");
	}

	@Test
	@DisplayName("GENERIC_TYPE_INIT_COMPLEX_NESTING")
	public void test0021() {
		String text = "Horse<Horse<String, Object>, Object>()";
		List<String> words = lexer.getSubWords(text, '(', ')', '[', ']', '{', '}');
		log.info(words.toString());
		assertTrue(words.size() == 3);
		int count = 0;
		assertEquals(words.get(count++), "Horse<Horse<String, Object>, Object>");
		assertEquals(words.get(count++), "(");
		assertEquals(words.get(count++), ")");
	}

}
