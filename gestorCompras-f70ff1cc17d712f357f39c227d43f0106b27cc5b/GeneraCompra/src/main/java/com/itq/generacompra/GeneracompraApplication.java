package com.itq.generacompra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GeneracompraApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeneracompraApplication.class, args);
	}

}
