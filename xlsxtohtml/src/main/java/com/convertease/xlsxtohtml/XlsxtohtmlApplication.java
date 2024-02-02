package com.convertease.xlsxtohtml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.convertease.xlsxtohtml")

public class XlsxtohtmlApplication {

	public static void main(String[] args) {
		SpringApplication.run(XlsxtohtmlApplication.class, args);
	}

}
