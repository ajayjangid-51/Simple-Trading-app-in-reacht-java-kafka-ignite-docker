# Trading Simulation Application

A learning-grade short-term trading simulation application demonstrating event-driven architecture with Kafka, Apache Ignite, and PostgreSQL.

## 🏗️ Architecture

```
React UI → Spring Boot REST API → Kafka Producer
                                      ↓
                                 Kafka Topic (trade_orders)
                                      ↓
                                 Kafka Consumer
                                      ↓
                    ┌─────────────────┴─────────────────┐
                    ↓                                     ↓
              Ignite Cache                        PostgreSQL
          (Real-time State)                    (Historical Data)
```

## 🛠️ Tech Stack

### Frontend
- React 18 with Vite
- Axios for API calls

### Backend
- Spring Boot 3.1.5 (Java 17)
- Spring Kafka
- PostgreSQL JDBC
- Apache Ignite (Thin Client)

### Infrastructure
- Docker & Docker Compose
- Kafka + Zookeeper
- Apache Ignite
- PostgreSQL

## 📋 Prerequisites

- Docker Desktop (or Docker Engine + Docker Compose)
- At least 4GB of free RAM
- Ports available: 3000, 8080, 9092, 5432, 10800, 2181

## 🚀 Quick Start

1. **Clone the repository** (if applicable) or navigate to the project directory

2. **Start all services with Docker Compose:**
   ```bash
   docker compose up --build
   ```

   This command will:
   - Build the Spring Boot backend
   - Build the React frontend
   - Start Zookeeper, Kafka, PostgreSQL, and Ignite
   - Initialize the database schema

3. **Wait for all services to be ready** (approximately 1-2 minutes)

4. **Access the application:**
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080

## 📝 Usage

### Placing a Trade

1. Open the frontend at http://localhost:3000
2. Select a symbol (NIFTY, BANKNIFTY, SBIN, RELIANCE)
3. Choose BUY or SELL
4. Enter quantity and price
5. Click "Place Order"

### Viewing Data

- **Open Positions**: Real-time position data from Ignite cache
- **Today's Analytics**: Aggregated daily statistics from PostgreSQL
- **Trades Today**: List of all trades executed today

## 🔌 API Endpoints

### Trade Operations
- `POST /api/trade` - Place a trade order
  ```json
  {
    "symbol": "NIFTY",
    "side": "BUY",
    "quantity": 50,
    "price": 22100
  }
  ```

### Position Operations
- `GET /api/positions` - Get all positions (from Ignite)
- `GET /api/positions/{symbol}` - Get position for a symbol

### Analytics
- `GET /api/analytics/daily` - Get daily analytics per symbol
- `GET /api/analytics/trades/today` - Get all trades for today

## 🐳 Docker Services

| Service | Port | Description |
|---------|------|-------------|
| Frontend | 3000 | React application |
| Backend | 8080 | Spring Boot API |
| Kafka | 9092 | Message broker |
| PostgreSQL | 5432 | Database |
| Ignite | 10800 | In-memory cache |
| Zookeeper | 2181 | Kafka coordination |

## 📊 How It Works

1. **Trade Placement**: User submits trade via React UI
2. **Kafka Producer**: Backend publishes trade event to `trade_orders` topic
3. **Kafka Consumer**: Asynchronously processes trade events:
   - Updates Ignite cache with position data (netQuantity, PnL)
   - Persists trade to PostgreSQL for analytics
4. **Real-time Updates**: Frontend polls backend every 2 seconds to display:
   - Current positions from Ignite
   - Historical trades from PostgreSQL

## 🔧 Configuration

### Environment Variables

Backend environment variables (set in docker-compose.yml):
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`: Kafka broker address
- `SPRING_DATASOURCE_URL`: PostgreSQL connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `IGNITE_ADDRESS`: Ignite server address

Frontend environment variables:
- `VITE_API_URL`: Backend API URL (default: http://localhost:8080)

## 🗄️ Database Schema

The `trades` table is automatically created on PostgreSQL startup:

```sql
CREATE TABLE IF NOT EXISTS trades (
  trade_id UUID PRIMARY KEY,
  symbol VARCHAR(20),
  side VARCHAR(10),
  quantity INT,
  price DOUBLE PRECISION,
  trade_time TIMESTAMP
);
```

## 🧪 Testing the Application

1. Place a BUY order for NIFTY (quantity: 50, price: 22100)
2. Place a SELL order for NIFTY (quantity: 30, price: 22200)
3. Check the positions - you should see:
   - Net Quantity: -20 (50 - 30)
   - PnL: -110000 (negative because BUY is negative, SELL is positive)

## 🛑 Stopping the Application

Press `Ctrl+C` in the terminal, or run:
```bash
docker compose down
```

To remove all data (including database):
```bash
docker compose down -v
```

## 📁 Project Structure

```
.
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/trading/
│   │   │   │   ├── controller/    # REST controllers
│   │   │   │   ├── service/       # Business logic
│   │   │   │   ├── kafka/         # Kafka producer/consumer
│   │   │   │   ├── ignite/        # Ignite client integration
│   │   │   │   ├── repository/    # JPA repositories
│   │   │   │   └── model/         # Data models
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── schema.sql
│   ├── Dockerfile
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── App.jsx
│   │   ├── main.jsx
│   │   └── index.css
│   ├── Dockerfile
│   ├── package.json
│   └── vite.config.js
├── docker-compose.yml
├── ignite-config.xml
└── README.md
```

## ⚠️ Important Notes

- This is a **PAPER TRADING** application - no real trading occurs
- No authentication/authorization implemented
- No WebSocket support - frontend polls backend
- No matching engine - trades are executed as-is
- No market data feeds - prices are user-entered
- Designed for learning purposes

## 🐛 Troubleshooting

### Services won't start
- Ensure Docker has enough resources (4GB+ RAM)
- Check if ports are already in use
- Try `docker compose down` and `docker compose up --build` again

### Backend can't connect to services
- Wait a bit longer for services to initialize
- Check service logs: `docker compose logs backend`
- Verify network connectivity: `docker network ls`

### Frontend can't reach backend
- Ensure `VITE_API_URL` is set correctly
- Check CORS settings in backend
- Verify backend is running: `curl http://localhost:8080/api/positions`

### Ignite connection issues
- Ensure Ignite container is running: `docker compose ps`
- Check Ignite logs: `docker compose logs ignite`
- Verify Ignite address in application.yml

## 📚 Learning Points

This application demonstrates:
- Event-driven architecture with Kafka
- Asynchronous message processing
- Real-time state management with in-memory cache (Ignite)
- Historical data persistence (PostgreSQL)
- Microservices communication via Docker networking
- Full-stack development with React and Spring Boot

## 📄 License

This is a learning project - feel free to use and modify as needed.

