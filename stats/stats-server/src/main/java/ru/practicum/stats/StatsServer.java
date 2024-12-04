package ru.practicum.stats;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "ru.practicum.statsdto")
public class StatsServer {
    public static void main(String[] args) {

        SpringApplication.run(StatsServer.class, args);
    }
}