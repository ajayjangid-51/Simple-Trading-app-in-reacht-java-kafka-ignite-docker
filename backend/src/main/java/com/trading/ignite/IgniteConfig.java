package com.trading.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * Ignite client configuration
 * Connects to Ignite cluster for real-time position tracking
 * Uses client mode to connect to remote Ignite cluster
 */
@Configuration
public class IgniteConfig {
    
    @Value("${ignite.address}")
    private String igniteAddress;
    
    /**
     * Creates Ignite client connection
     * Client mode connects to remote Ignite cluster without joining as a node
     */
    @Bean(destroyMethod = "close")
    public Ignite ignite() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        
        // Set client mode - this node won't store data, just connect to cluster
        cfg.setClientMode(true);
        
        // Configure discovery to connect to Ignite server
        // Parse address (format: host:port) - extract hostname for discovery
        String[] parts = igniteAddress.split(":");
        String host = parts.length > 0 ? parts[0] : "ignite";
        
        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        // Use standard Ignite discovery port range (47500-47509)
        // In Docker, use the service name 'ignite' for discovery
        ipFinder.setAddresses(Collections.singletonList("ignite:47500..47509"));
        discoverySpi.setIpFinder(ipFinder);
        cfg.setDiscoverySpi(discoverySpi);
        
        return Ignition.start(cfg);
    }
}

