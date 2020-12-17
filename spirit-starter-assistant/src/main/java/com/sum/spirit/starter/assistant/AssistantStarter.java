package com.sum.spirit.starter.assistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sum.spirit")
public class AssistantStarter {
	public static void main(String[] args) {
		SpringApplication.run(AssistantStarter.class, args);
	}
}
