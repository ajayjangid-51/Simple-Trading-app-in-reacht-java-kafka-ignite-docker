package com.trading.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Position model for Ignite cache
 * Tracks real-time position state per symbol
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer netQuantity; // Net position quantity
    private Double pnl; // Profit and Loss
}

