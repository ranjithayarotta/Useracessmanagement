package com.mindlabs.reportgenearator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReportGeneratorApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(ReportGeneratorApplication.class, args);

	}

	public void run(String... args) throws Exception {
		System.out.println("Boot is up..");

	}
}
