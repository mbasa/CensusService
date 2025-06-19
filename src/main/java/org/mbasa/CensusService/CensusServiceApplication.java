package org.mbasa.CensusService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Japanese Census Service", version = "1.0.0", description = "Retrieves Japanese Census Data", contact = @Contact(name = "Mario Basa", email = "mario.basa@gmail.com"), license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")))
public class CensusServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CensusServiceApplication.class, args);
	}

}
