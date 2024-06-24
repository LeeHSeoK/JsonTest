package org.zerock.jsontest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@SpringBootApplication
//public class JsonTestApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(JsonTestApplication.class, args);
//    }
//
//}

@SpringBootApplication
@EnableJpaAuditing
public class JsonTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsonTestApplication.class, args);
    }

}