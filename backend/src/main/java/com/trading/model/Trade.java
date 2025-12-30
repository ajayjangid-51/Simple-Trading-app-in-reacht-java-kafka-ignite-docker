package com.trading.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Trade entity for PostgreSQL persistence
 * Stores historical trade data for analytics
 */
@Entity
@Table(name = "trades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
    @Id
    @Column(name = "trade_id")
    private UUID tradeId;
    
    @Column(name = "symbol", nullable = false)
    private String symbol;
    
    @Column(name = "side", nullable = false)
    private String side;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @Column(name = "price", nullable = false)
    private Double price;
    
    @Column(name = "trade_time", nullable = false)
    private LocalDateTime tradeTime;
}

