package com.trading.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Request DTO for trade placement
 */
@Data
public class TradeRequest {
    @NotBlank(message = "Symbol is required")
    private String symbol;
    
    @NotBlank(message = "Side is required (BUY or SELL)")
    private String side;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;
}

