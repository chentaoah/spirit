package com.sum.spirit.core.lexer.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sum.spirit.core.api.ElementBuilder;

@SpringBootTest
@DisplayName("语法分析器测试")
public class ElementTest {

	@Autowired
	public ElementBuilder builder;

	@Test
	@DisplayName("方法声明")
	public void test0000() {

	}

}
