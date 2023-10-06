package com.bank.podo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
<<<<<<<< HEAD:Podo_Bank/backend/auth/src/main/java/com/bank/podo/PodoBankAuthApplication.java
@EnableDiscoveryClient
public class PodoBankAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(PodoBankAuthApplication.class, args);
========
public class PodoBankOpenBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PodoBankOpenBankingApplication.class, args);
>>>>>>>> develop:Podo_Bank/backend/openbanking/src/main/java/com/bank/podo/PodoBankOpenBankingApplication.java
    }

}
