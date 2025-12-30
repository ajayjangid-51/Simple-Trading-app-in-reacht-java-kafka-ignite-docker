package com.trading.kafka;

import com.trading.model.TradeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Kafka producer for publishing trade events
 * Publishes trade orders to trade_orders topic for async processing
 */
@Service
public class TradeProducer {
    
    private static final String TOPIC = "trade_orders";
    private static final Logger logger = LoggerFactory.getLogger(TradeProducer.class);
    
    @Autowired
    private KafkaTemplate<String, TradeEvent> kafkaTemplate;
    
    /**
     * Publishes trade event to Kafka topic
     * This is async - consumer will process it
     */
    public void publishTrade(TradeEvent tradeEvent) {
        logger.info("Publishing trade event: {}", tradeEvent);
        
        CompletableFuture<SendResult<String, TradeEvent>> future = 
            kafkaTemplate.send(TOPIC, tradeEvent.getTradeId().toString(), tradeEvent);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Trade event published successfully: {}", result.getRecordMetadata());
            } else {
                logger.error("Failed to publish trade event", ex);
            }
        });
    }
}

