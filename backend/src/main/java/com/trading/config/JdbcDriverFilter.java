package com.trading.config;

import org.springframework.context.annotation.Configuration;

import java.sql.DriverManager;
import java.sql.Driver;
import java.util.Enumeration;

/**
 * Configuration to prevent Ignite JDBC driver from being loaded
 * This class runs early to deregister Ignite JDBC driver if it gets loaded
 */
@Configuration
public class JdbcDriverFilter {
    
    static {
        // Deregister Ignite JDBC driver if it gets loaded
        // This prevents ExceptionInInitializerError during startup
        try {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                if (driver.getClass().getName().contains("IgniteJdbcThinDriver")) {
                    try {
                        DriverManager.deregisterDriver(driver);
                    } catch (Exception e) {
                        // Ignore - driver might not be registered yet
                    }
                }
            }
        } catch (Exception e) {
            // Ignore - this is best effort
        }
    }
}
