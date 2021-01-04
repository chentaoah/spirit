package com.sum.spirit.starter.kit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sum.spirit")
public class SpiritKitStarter {
	public static void main(String[] args) {
		SpringApplication.run(SpiritKitStarter.class, args);
	}
}
