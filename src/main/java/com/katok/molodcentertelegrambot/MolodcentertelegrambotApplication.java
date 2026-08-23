package com.katok.molodcentertelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MolodcentertelegrambotApplication {
	public static void main(String[] args) {
		SpringApplication.run(MolodcentertelegrambotApplication.class, args);
	}
}
