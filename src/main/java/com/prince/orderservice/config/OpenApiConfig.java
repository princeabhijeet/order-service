package com.prince.orderservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
	
	//Swagger3 OpenAPI UI : http://localhost:${server.port}/swagger-ui/index.html#/
	
	@Value("${spring.application.name}")
	private String title;

	@Bean
	OpenAPI customOpenAPI() {
		
		Contact contact = new Contact();
		contact.setName("Prince Abhijeet");
		contact.setEmail("princeabhijeet1994@gmail.com");
		
		Info info = new Info();
		info.title(title.toUpperCase().concat(" APIs"));
		info.description("REST APIs : " + title);
		info.contact(contact);
		
		OpenAPI openApi = new OpenAPI();
		openApi.info(info);
		
		return openApi;
	}
}
