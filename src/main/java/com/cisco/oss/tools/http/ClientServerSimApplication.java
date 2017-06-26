package com.cisco.oss.tools.http;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.AsyncRestTemplate;

@SpringBootApplication
public class ClientServerSimApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientServerSimApplication.class, args);
	}


	@Bean
	@Profile("client")
	public AsyncRestTemplate restTemplate(){
		return new AsyncRestTemplate();
	}

}
