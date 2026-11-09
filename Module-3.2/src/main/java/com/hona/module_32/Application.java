package com.hona.module_32;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
//.\mvnw.cmd clean test
// .\mvnw.cmd jacoco:report
//Test-Path .\target\site\jacoco\index.html
//start .\target\site\jacoco\index.html