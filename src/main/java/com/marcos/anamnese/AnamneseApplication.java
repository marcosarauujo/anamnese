package com.marcos.anamnese;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AnamneseApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnamneseApplication.class, args);
	}

}
