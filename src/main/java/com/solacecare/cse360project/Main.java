package com.solacecare.cse360project;

import com.solacecare.cse360project.main.MainJFX;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(Main.class, args);
        MainJFX.main(args);
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }
}