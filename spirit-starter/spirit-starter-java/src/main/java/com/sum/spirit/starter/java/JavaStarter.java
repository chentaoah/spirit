package com.sum.spirit.starter.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sum.spirit")
public class JavaStarter {
	public static void main(String[] args) {
		SpringApplication.run(JavaStarter.class, args);
	}
}
