package com.yongy.dotoriUserService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient

public class DotoriUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DotoriUserServiceApplication.class, args);
	}

}
