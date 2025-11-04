package com.itmentorcommunityplatform.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }


    public List<Integer> getSquaresOfEvenNumbers(List<Integer> numbers){
        return numbers.stream().filter(n-> n%2 == 0).map(n-> n*n).sorted().limit(3).toList();
    }
}
