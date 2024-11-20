package com.itq.autorizacompragerencial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AutorizacompragerencialApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutorizacompragerencialApplication.class, args);
    }
}