package com.itq.autorizacompraoperativo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AutorizacompraoperativoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutorizacompraoperativoApplication.class, args);
	}

}
