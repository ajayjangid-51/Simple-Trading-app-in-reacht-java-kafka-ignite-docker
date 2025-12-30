package com.trading.config;

import org.apache.catalina.Context;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * Configuration to suppress JDBC driver cleanup warnings during shutdown
 * This prevents NoClassDefFoundError for IgniteJdbcThinDriver during application shutdown
 * 
 * Note: This is a harmless warning that occurs during shutdown when Tomcat tries to
 * unregister JDBC drivers. Ignite registers a JDBC driver but the class isn't available
 * during cleanup, causing this non-critical error.
 * 
 * If this configuration causes build issues, it can be safely removed - the warning
 * is harmless and doesn't affect application functionality.
 */
@Configuration
public class ShutdownConfig {
    
    /**
     * Customize Tomcat to skip JDBC driver cleanup
     * This prevents the warning about IgniteJdbcThinDriver not being found during shutdown
     * Uses reflection to safely call the method if it exists
     */
    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addContextCustomizers(new TomcatContextCustomizer() {
            @Override
            public void customize(Context context) {
                // Disable JDBC driver cleanup to prevent Ignite JDBC driver warnings
                // This is safe because we're not using Ignite as a JDBC data source
                try {
                    // Use reflection to call setClearReferencesJdbc if available
                    // This avoids compilation issues with StandardContext
                    Method method = context.getClass().getMethod("setClearReferencesJdbc", boolean.class);
                    method.invoke(context, false);
                } catch (NoSuchMethodException | SecurityException e) {
                    // Method doesn't exist - that's okay, just continue
                    // This is a best-effort attempt to suppress the warning
                } catch (Exception e) {
                    // Any other exception - log but don't fail
                    // The warning is harmless anyway
                }
            }
        });
        return factory;
    }
}

