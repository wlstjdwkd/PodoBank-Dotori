package com.bank.podo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PodoBankAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(PodoBankAccountApplication.class, args);
    }

}
