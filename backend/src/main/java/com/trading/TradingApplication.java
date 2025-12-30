package com.trading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TradingApplication {
    public static void main(String[] args) {
        // Prevent Ignite JDBC driver from being loaded during startup
        // This avoids ExceptionInInitializerError when Spring Boot scans JDBC drivers
        // System.setProperty("ignite.jdbc.driver.skip", "true");
        
        SpringApplication.run(TradingApplication.class, args);
    }
}

