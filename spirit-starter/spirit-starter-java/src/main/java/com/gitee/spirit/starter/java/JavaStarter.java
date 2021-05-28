package com.gitee.spirit.starter.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gitee.spirit")
public class JavaStarter {
	public static void main(String[] args) {
		SpringApplication.run(JavaStarter.class, args);
	}
}
