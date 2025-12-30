-- Create trades table for historical analytics
CREATE TABLE IF NOT EXISTS trades (
  trade_id UUID PRIMARY KEY,
  symbol VARCHAR(20) NOT NULL,
  side VARCHAR(10) NOT NULL,
  quantity INT NOT NULL,
  price DOUBLE PRECISION NOT NULL,
  trade_time TIMESTAMP NOT NULL
);

-- Create index on symbol and trade_time for faster analytics queries
CREATE INDEX IF NOT EXISTS idx_trades_symbol_time ON trades(symbol, trade_time);

