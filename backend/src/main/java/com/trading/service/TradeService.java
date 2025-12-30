package com.trading.service;

import com.trading.kafka.TradeProducer;
import com.trading.model.TradeEvent;
import com.trading.model.TradeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service for handling trade operations
 * Orchestrates trade placement and Kafka publishing
 */
@Service
public class TradeService {
    
    @Autowired
    private TradeProducer tradeProducer;
    
    /**
     * Places a trade by publishing to Kafka
     * Actual processing happens asynchronously in the consumer
     */
    public TradeEvent placeTrade(TradeRequest request) {
        // Create trade event
        TradeEvent tradeEvent = new TradeEvent();
        tradeEvent.setTradeId(UUID.randomUUID());
        tradeEvent.setSymbol(request.getSymbol());
        tradeEvent.setSide(request.getSide());
        tradeEvent.setQuantity(request.getQuantity());
        tradeEvent.setPrice(request.getPrice());
        tradeEvent.setTimestamp(System.currentTimeMillis());
        
        // Publish to Kafka
        tradeProducer.publishTrade(tradeEvent);
        
        return tradeEvent;
    }
}

