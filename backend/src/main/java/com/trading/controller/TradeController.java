package com.trading.controller;

import com.trading.model.TradeEvent;
import com.trading.model.TradeRequest;
import com.trading.service.TradeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for trade operations
 */
@RestController
@RequestMapping("/api/trade")
@CrossOrigin(origins = "*")
public class TradeController {
    
    @Autowired
    private TradeService tradeService;
    
    /**
     * Place a trade order
     * Publishes trade event to Kafka for async processing
     */
    @PostMapping
    public ResponseEntity<TradeEvent> placeTrade(@Valid @RequestBody TradeRequest request) {
        TradeEvent tradeEvent = tradeService.placeTrade(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tradeEvent);
    }
}

