package com.trading.repository;

import com.trading.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Repository for trade persistence operations, repository means here database
 */
@Repository
public interface TradeRepository extends JpaRepository<Trade, UUID> {
    
    /**
     * Get all trades for today grouped by symbol
     * Returns total quantity and PnL per symbol
     */
    @Query("SELECT t.symbol, " +
           "SUM(CASE WHEN t.side = 'BUY' THEN -t.quantity ELSE t.quantity END) as totalQuantity, " +
           "SUM(CASE WHEN t.side = 'BUY' THEN -t.quantity * t.price ELSE t.quantity * t.price END) as totalPnl " +
           "FROM Trade t " +
           "WHERE DATE(t.tradeTime) = :date " +
           "GROUP BY t.symbol")
    List<Object[]> getDailyAnalytics(@Param("date") LocalDate date);
    
    /**
     * Get all trades for today
     */
    @Query("SELECT t FROM Trade t WHERE DATE(t.tradeTime) = :date ORDER BY t.tradeTime DESC")
    List<Trade> findAllByDate(@Param("date") LocalDate date);
}







