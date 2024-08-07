package com.example.societymanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.example.societymanager.controller", "com.example.societymanager.service", "com.example.societymanager.config"})
public class SocietymanagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SocietymanagerApplication.class, args);
	}

}
