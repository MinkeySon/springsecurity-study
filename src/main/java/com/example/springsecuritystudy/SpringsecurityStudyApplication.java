package com.example.springsecuritystudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringsecurityStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringsecurityStudyApplication.class, args);
    }

}
