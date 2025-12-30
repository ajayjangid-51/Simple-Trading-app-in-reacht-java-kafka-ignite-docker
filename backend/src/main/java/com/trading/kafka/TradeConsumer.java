package com.trading.kafka;

import com.trading.ignite.PositionService;
import com.trading.model.Trade;
import com.trading.model.TradeEvent;
import com.trading.repository.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Kafka consumer for processing trade events
 * Processes trades asynchronously:
 * 1. Updates Ignite cache with position data
 * 2. Persists trade to PostgreSQL
 */
@Service
public class TradeConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(TradeConsumer.class);
    
    @Autowired
    private PositionService positionService;
    
    @Autowired
    private TradeRepository tradeRepository;
    
    /**
     * Consumes trade events from Kafka topic
     * This method is called automatically when a message arrives
     */
    @KafkaListener(topics = "trade_orders", groupId = "trading-consumer-group")
    public void consumeTrade(TradeEvent tradeEvent) {
        logger.info("Consuming trade event: {}", tradeEvent);
        
        try {
            // Step 1: Update Ignite cache with position data
            positionService.updatePosition(
                tradeEvent.getSymbol(),
                tradeEvent.getSide(),
                tradeEvent.getQuantity(),
                tradeEvent.getPrice()
            );
            logger.info("Updated Ignite position for symbol: {}", tradeEvent.getSymbol());
            
            // Step 2: Persist trade to PostgreSQL
            Trade trade = new Trade();
            trade.setTradeId(tradeEvent.getTradeId());
            trade.setSymbol(tradeEvent.getSymbol());
            trade.setSide(tradeEvent.getSide());
            trade.setQuantity(tradeEvent.getQuantity());
            trade.setPrice(tradeEvent.getPrice());
            
            // Convert timestamp to LocalDateTime
            LocalDateTime tradeTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(tradeEvent.getTimestamp()),
                ZoneId.systemDefault()
            );
            trade.setTradeTime(tradeTime);
            
            tradeRepository.save(trade);
            logger.info("Persisted trade to PostgreSQL: {}", trade.getTradeId());
            
        } catch (Exception e) {
            logger.error("Error processing trade event: {}", tradeEvent, e);
            // In production, you might want to implement retry logic or dead letter queue
        }
    }
}

