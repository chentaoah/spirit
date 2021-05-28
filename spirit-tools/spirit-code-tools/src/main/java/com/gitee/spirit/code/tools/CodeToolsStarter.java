package com.gitee.spirit.code.tools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gitee.spirit")
public class CodeToolsStarter {
	public static void main(String[] args) {
		SpringApplication.run(CodeToolsStarter.class, args);
	}
}
