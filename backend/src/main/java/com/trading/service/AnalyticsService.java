package com.trading.service;

import com.trading.ignite.PositionService;
import com.trading.model.AnalyticsResponse;
import com.trading.model.Position;
import com.trading.model.Trade;
import com.trading.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for analytics operations
 * Combines data from PostgreSQL (historical) and Ignite (real-time)
 */
@Service
public class AnalyticsService {
    
    @Autowired
    private TradeRepository tradeRepository;
    
    @Autowired
    private PositionService positionService;
    
    /**
     * Get daily analytics per symbol
     * Returns total quantity and PnL for today's trades
     */
    public AnalyticsResponse getDailyAnalytics() {
        LocalDate today = LocalDate.now();
        List<Object[]> results = tradeRepository.getDailyAnalytics(today);
        
        Map<String, AnalyticsResponse.SymbolAnalytics> analytics = new HashMap<>();
        
        for (Object[] row : results) {
            String symbol = (String) row[0];
            Long totalQuantity = ((Number) row[1]).longValue();
            Double totalPnl = ((Number) row[2]).doubleValue();
            
            analytics.put(symbol, new AnalyticsResponse.SymbolAnalytics(
                totalQuantity.intValue(),
                totalPnl
            ));
        }
        
        return new AnalyticsResponse(analytics);
    }
    
    /**
     * Get all trades for today
     */
    public List<Trade> getTradesToday() {
        return tradeRepository.findAllByDate(LocalDate.now());
    }
}

