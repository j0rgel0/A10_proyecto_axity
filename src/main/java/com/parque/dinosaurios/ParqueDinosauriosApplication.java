package com.parque.dinosaurios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ParqueDinosauriosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParqueDinosauriosApplication.class, args);
	}

}
