package com.sum.spirit.lexer.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sum.spirit.api.Lexer;

@SpringBootTest
@DisplayName("词法分析器测试")
public class LexerTest {

	@Autowired
	public Lexer lexer;

	@Test
	@DisplayName("集合测试")
	public void test() {
		String text = "map = {\"name\":\"tao.chen\", \"age\":29}";
		List<String> words = lexer.getWords(text);
		assertTrue(words.size() == 3);
		assertEquals(words.get(0), "map");
		assertEquals(words.get(1), "=");
		assertEquals(words.get(2), "{\"name\":\"tao.chen\", \"age\":29}");
	}

	@Test
	@DisplayName("集合内部拆分测试")
	public void test1() {
		String text = "{\"name\":\"tao.chen\", \"age\":29}";
		List<String> words = lexer.getSubWords(text, '(', ')', '[', ']', '{', '}');
		assertTrue(words.size() == 9);
		assertEquals(words.get(0), "{");
		assertEquals(words.get(1), "\"name\"");
		assertEquals(words.get(2), ":");
		assertEquals(words.get(3), "\"tao.chen\"");
		assertEquals(words.get(4), ",");
		assertEquals(words.get(5), "\"age\"");
		assertEquals(words.get(6), ":");
		assertEquals(words.get(7), "29");
		assertEquals(words.get(8), "}");
	}

}
