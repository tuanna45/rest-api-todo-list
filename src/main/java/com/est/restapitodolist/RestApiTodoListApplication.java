package com.est.restapitodolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@EntityScan(basePackageClasses = {RestApiTodoListApplication.class, Jsr310JpaConverters.class})
public class RestApiTodoListApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiTodoListApplication.class, args);
    }
}
