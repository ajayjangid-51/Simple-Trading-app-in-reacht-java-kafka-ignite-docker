package com.trading.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Response DTO for analytics endpoints
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {
    private Map<String, SymbolAnalytics> dailyAnalytics;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SymbolAnalytics {
        private Integer totalQuantity;
        private Double totalPnl;
    }
}

