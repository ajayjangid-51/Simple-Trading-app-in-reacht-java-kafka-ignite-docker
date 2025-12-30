# Quick Start Guide

## ✅ Services Running

Once `docker compose up --build` completes successfully, you should have:

- ✅ **Zookeeper** - Running on port 2181
- ✅ **Kafka** - Running on port 9092
- ✅ **PostgreSQL** - Running on port 5432
- ✅ **Ignite** - Running on ports 10800 (thin client) and 8081 (web console)
- ✅ **Backend** - Running on port 8080
- ✅ **Frontend** - Running on port 3000

## 🧪 Testing the Application

### 1. Access the Frontend

Open your browser and go to: **http://localhost:3000**

### 2. Place a Test Trade

- Select symbol: **NIFTY**
- Side: **BUY**
- Quantity: **50**
- Price: **22100**
- Click "Place BUY Order"

### 3. Verify the Trade

After placing the trade, you should see:

- **Open Positions** section shows NIFTY with:
  - Net Quantity: -50 (negative because BUY decreases position)
  - PnL: -1,105,000 (negative because BUY is a cost)

### 4. Place Another Trade

- Select symbol: **NIFTY**
- Side: **SELL**
- Quantity: **30**
- Price: **22200**
- Click "Place SELL Order"

### 5. Check Updated Position

Now you should see:

- Net Quantity: -20 (was -50, sold 30, so -50 + 30 = -20)
- PnL: -439,000 (was -1,105,000, sold 30 at 22200 adds +666,000, so -1,105,000 + 666,000 = -439,000)

### 6. Check Today's Analytics

The "Today's Analytics" section should show aggregated data for all symbols traded today.

### 7. Check Trades Today

The "Trades Today" section should list all trades executed today in chronological order.

## 🔍 Verifying Services

### Check Backend Health

```bash
curl http://localhost:8080/api/positions
```

Should return: `{}` (empty positions initially)

### Check Kafka Topic

```bash
docker exec -it kafka kafka-topics --list --bootstrap-server localhost:9092
```

Should show: `trade_orders`

### Check PostgreSQL

```bash
docker exec -it postgres psql -U trading_user -d trading_db -c "SELECT COUNT(*) FROM trades;"
```

### Check Ignite Web Console

Open: **http://localhost:8081** (if available)

## 🐛 Troubleshooting

### If positions don't update:

1. Check backend logs: `docker compose logs backend`
2. Check Kafka consumer logs in backend output
3. Verify Ignite connection in backend logs

### If trades don't appear:

1. Check Kafka producer logs
2. Verify Kafka topic exists
3. Check PostgreSQL connection

### If frontend can't connect:

1. Verify backend is running: `curl http://localhost:8080/api/positions`
2. Check CORS settings (should be enabled)
3. Check frontend logs: `docker compose logs frontend`

## 📊 Understanding the Flow

1. **User places trade** → Frontend sends POST to `/api/trade`
2. **Backend publishes** → Trade event sent to Kafka `trade_orders` topic
3. **Kafka Consumer** → Processes trade asynchronously:
   - Updates Ignite cache (real-time positions)
   - Saves to PostgreSQL (historical data)
4. **Frontend polls** → Every 2 seconds fetches:
   - Positions from Ignite via `/api/positions`
   - Analytics from PostgreSQL via `/api/analytics/daily`
   - Trades from PostgreSQL via `/api/analytics/trades/today`

## 🎯 Next Steps

- Try different symbols (BANKNIFTY, SBIN, RELIANCE)
- Place multiple trades and watch positions update in real-time
- Check the database to see historical trades
- Monitor Kafka messages (optional: use Kafka UI tools)

Enjoy your trading simulation! 🚀
