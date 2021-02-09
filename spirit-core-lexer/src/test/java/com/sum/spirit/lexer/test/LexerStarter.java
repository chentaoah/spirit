package com.sum.spirit.lexer.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sum.spirit")
public class LexerStarter {
	public static void main(String[] args) {
		SpringApplication.run(LexerStarter.class, args);
	}
}
