package com.example.userservice;

import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(UserRepository userRepository) {
        return args -> {
            User user = new User(null, "danylo", "danylo@example.com", "password123", "USER");
            if (!userRepository.existsByUsername("danylo")) {
                userRepository.save(user);
            }
        };
    }
}
