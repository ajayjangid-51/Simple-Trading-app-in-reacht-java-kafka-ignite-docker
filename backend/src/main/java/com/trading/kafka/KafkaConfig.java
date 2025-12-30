package com.trading.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Kafka topic configuration
 * Ensures trade_orders topic exists
 * Note: Kafka will auto-create topics by default, but this makes it explicit
 */
@Configuration
public class KafkaConfig {
    
    private static final String TOPIC_NAME = "trade_orders";
    
    @Bean
    public NewTopic tradeOrdersTopic() {
        return TopicBuilder.name(TOPIC_NAME)
                .partitions(1)
                .replicas(1)
                .build();
    }
}

