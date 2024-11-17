package com.biblioteca.db;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class Biblioteca1Application {

	public static void main(String[] args) {
		SpringApplication.run(Biblioteca1Application.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Library Management API")
						.version("1.0")
						.description("API para o gerenciamento de uma biblioteca escolar.")
				);
	}

}
