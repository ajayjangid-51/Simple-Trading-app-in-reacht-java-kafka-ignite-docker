package com.trading.controller;

import com.trading.model.AnalyticsResponse;
import com.trading.model.Trade;
import com.trading.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for analytics operations
 */
@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {
    
    @Autowired
    private AnalyticsService analyticsService;
    
    /**
     * Get daily analytics per symbol
     * Returns total quantity and PnL for today
     */
    @GetMapping("/daily")
    public ResponseEntity<AnalyticsResponse> getDailyAnalytics() {
        AnalyticsResponse response = analyticsService.getDailyAnalytics();
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get all trades for today
     */
    @GetMapping("/trades/today")
    public ResponseEntity<List<Trade>> getTradesToday() {
        List<Trade> trades = analyticsService.getTradesToday();
        return ResponseEntity.ok(trades);
    }
}

