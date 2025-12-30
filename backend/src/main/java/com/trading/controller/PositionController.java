package com.trading.controller;

import com.trading.ignite.PositionService;
import com.trading.model.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for position operations
 * Returns real-time position data from Ignite
 */
@RestController
@RequestMapping("/api/positions")
@CrossOrigin(origins = "*")
public class PositionController {
    
    @Autowired
    private PositionService positionService;
    
    /**
     * Get all positions from Ignite
     * Returns real-time position data
     */
    @GetMapping
    public ResponseEntity<Map<String, Position>> getAllPositions() {
        Map<String, Position> positions = positionService.getAllPositions();
        return ResponseEntity.ok(positions);
    }
    
    /**
     * Get position for a specific symbol
     */
    @GetMapping("/{symbol}")
    public ResponseEntity<Position> getPosition(@PathVariable String symbol) {
        Position position = positionService.getPosition(symbol);
        return ResponseEntity.ok(position);
    }
}

