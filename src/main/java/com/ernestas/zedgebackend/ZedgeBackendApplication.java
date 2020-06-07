package com.ernestas.zedgebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZedgeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZedgeBackendApplication.class, args);
	}

}
