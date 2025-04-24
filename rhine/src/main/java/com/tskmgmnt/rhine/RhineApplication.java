package com.tskmgmnt.rhine;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Rhine Task Manager REST API Documentation",
				description = "Rhine Task Manager Spring Boot REST API Documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Onanta",
						email = "therhineapp@gmail.com",
						url = "https://www.rhine.com"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://www.apache.org/licenses/LICENSE-2.0"
				)
		)
)
public class RhineApplication {
	public static void main(String[] args) {
		SpringApplication.run(RhineApplication.class, args);
	}
}
