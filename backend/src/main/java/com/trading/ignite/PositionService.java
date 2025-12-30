package com.trading.ignite;

import com.trading.model.Position;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for managing positions in Ignite cache
 * Handles real-time position updates and retrieval
 */
@Service
public class PositionService {
    
    private static final String CACHE_NAME = "positions";
    
    @Autowired
    private Ignite ignite;
    
    /**
     * Get Ignite cache for positions
     */
    private IgniteCache<String, Position> getCache() {
        return ignite.getOrCreateCache(CACHE_NAME);
    }
    
    /**
     * Get current position for a symbol
     */
    public Position getPosition(String symbol) {
        IgniteCache<String, Position> cache = getCache();
        Position position = cache.get(symbol);
        return position != null ? position : new Position(0, 0.0);
    }
    
    /**
     * Update position based on trade
     * BUY: decreases netQuantity, decreases PnL (negative)
     * SELL: increases netQuantity, increases PnL (positive)
     */
    public void updatePosition(String symbol, String side, Integer quantity, Double price) {
        IgniteCache<String, Position> cache = getCache();
        
        // Get current position or create new one
        Position currentPosition = getPosition(symbol);
        
        int newNetQuantity;
        double newPnl;
        
        if ("BUY".equalsIgnoreCase(side)) {
            // BUY: negative impact on quantity and PnL
            newNetQuantity = currentPosition.getNetQuantity() - quantity;
            newPnl = currentPosition.getPnl() - (quantity * price);
        } else {
            // SELL: positive impact on quantity and PnL
            newNetQuantity = currentPosition.getNetQuantity() + quantity;
            newPnl = currentPosition.getPnl() + (quantity * price);
        }
        
        Position updatedPosition = new Position(newNetQuantity, newPnl);
        cache.put(symbol, updatedPosition);
    }
    
    /**
     * Get all positions
     */
    public java.util.Map<String, Position> getAllPositions() {
        IgniteCache<String, Position> cache = getCache();
        java.util.Map<String, Position> positions = new java.util.HashMap<>();
        
        // Iterate through cache entries
        cache.forEach(entry -> positions.put(entry.getKey(), entry.getValue()));
        
        return positions;
    }
}

