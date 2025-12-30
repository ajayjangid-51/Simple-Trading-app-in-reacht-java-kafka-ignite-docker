package com.trading.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Trade event model for Kafka messaging
 * Represents a trade order placed by the user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeEvent {
    @JsonProperty("tradeId")
    private UUID tradeId;
    
    @JsonProperty("symbol")
    private String symbol;
    
    @JsonProperty("side")
    private String side; // BUY or SELL
    
    @JsonProperty("quantity")
    private Integer quantity;
    
    @JsonProperty("price")
    private Double price;
    
    @JsonProperty("timestamp")
    private Long timestamp;
}

