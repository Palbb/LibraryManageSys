package com.LibraryManagmentSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        // Устанавливаем часовой пояс для всей JVM ПЕРЕД запуском Spring
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Kyiv"));
        SpringApplication.run(Main.class, args);
    }
}