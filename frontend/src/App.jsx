import React, { useState, useEffect } from "react";
import axios from "axios";
import "./index.css";

const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";

function App() {
	const [formData, setFormData] = useState({
		symbol: "NIFTY",
		side: "BUY",
		quantity: "",
		price: "",
	});
	const [positions, setPositions] = useState({});
	const [tradesToday, setTradesToday] = useState([]);
	const [dailyAnalytics, setDailyAnalytics] = useState({});
	const [loading, setLoading] = useState(false);
	const [message, setMessage] = useState(null);
	const [messageType, setMessageType] = useState(null);

	// Fetch positions from Ignite
	const fetchPositions = async () => {
		try {
			const response = await axios.get(`${API_URL}/api/positions`);
			setPositions(response.data);
		} catch (error) {
			console.error("Error fetching positions:", error);
		}
	};

	// Fetch trades for today
	const fetchTradesToday = async () => {
		try {
			const response = await axios.get(`${API_URL}/api/analytics/trades/today`);
			setTradesToday(response.data);
		} catch (error) {
			console.error("Error fetching trades:", error);
		}
	};

	// Fetch daily analytics
	const fetchDailyAnalytics = async () => {
		try {
			const response = await axios.get(`${API_URL}/api/analytics/daily`);
			setDailyAnalytics(response.data.dailyAnalytics || {});
		} catch (error) {
			console.error("Error fetching analytics:", error);
		}
	};

	// Load data on mount and refresh periodically
	useEffect(() => {
		fetchPositions();
		fetchTradesToday();
		fetchDailyAnalytics();

		// Refresh data every 2 seconds
		const interval = setInterval(() => {
			fetchPositions();
			fetchTradesToday();
			fetchDailyAnalytics();
		}, 2000);

		return () => clearInterval(interval);
	}, []);

	const handleInputChange = (e) => {
		const { name, value } = e.target;
		setFormData((prev) => ({
			...prev,
			[name]: name === "quantity" || name === "price" ? value : value,
		}));
	};

	const handleSubmit = async (e) => {
		e.preventDefault();
		setLoading(true);
		setMessage(null);

		try {
			const tradeData = {
				symbol: formData.symbol,
				side: formData.side,
				quantity: parseInt(formData.quantity),
				price: parseFloat(formData.price),
			};

			await axios.post(`${API_URL}/api/trade`, tradeData);

			setMessage("Trade placed successfully!");
			setMessageType("success");

			// Reset form
			setFormData((prev) => ({
				...prev,
				quantity: "",
				price: "",
			}));

			// Refresh data after a short delay
			setTimeout(() => {
				fetchPositions();
				fetchTradesToday();
				fetchDailyAnalytics();
			}, 500);
		} catch (error) {
			setMessage(error.response?.data?.message || "Error placing trade");
			setMessageType("error");
			console.error("Error placing trade:", error);
		} finally {
			setLoading(false);
			setTimeout(() => {
				setMessage(null);
			}, 3000);
		}
	};

	const formatCurrency = (value) => {
		if (value === null || value === undefined) return "0.00";
		return new Intl.NumberFormat("en-IN", {
			minimumFractionDigits: 2,
			maximumFractionDigits: 2,
		}).format(value);
	};

	const formatDate = (dateString) => {
		return new Date(dateString).toLocaleString();
	};

	return (
		<div className="container">
			<div className="header">
				<h1>📈 Trading Simulation App</h1>
				<p>Paper Trading Platform - Real-time Position Tracking</p>
			</div>

			<div className="grid">
				{/* Trade Placement Form */}
				<div className="card">
					<h2>Place Trade</h2>
					{message && (
						<div className={messageType === "error" ? "error" : "success"}>
							{message}
						</div>
					)}
					<form onSubmit={handleSubmit}>
						<div className="form-group">
							<label>Symbol</label>
							<select
								name="symboly"
								value={formData.symbol}
								onChange={handleInputChange}
								required
							>
								<option value="NIFTY">NIFTY</option>
								<option value="BANKNIFTY">BANKNIFTY</option>
								<option value="SBIN">SBIN</option>
								<option value="RELIANCE">RELIANCE</option>
							</select>
						</div>

						<div className="form-group">
							<label>Side</label>
							<select
								name="side"
								value={formData.side}
								onChange={handleInputChange}
								required
							>
								<option value="BUY">BUY</option>
								<option value="SELL">SELL</option>
							</select>
						</div>

						<div className="form-group">
							<label>Quantity</label>
							<input
								type="number"
								name="quantity"
								value={formData.quantity}
								onChange={handleInputChange}
								min="1"
								required
							/>
						</div>

						<div className="form-group">
							<label>Price</label>
							<input
								type="number"
								name="price"
								value={formData.price}
								onChange={handleInputChange}
								min="0"
								step="0.01"
								required
							/>
						</div>

						<button
							type="submit"
							className={`btn ${
								formData.side === "BUY" ? "btn-buy" : "btn-sell"
							}`}
							disabled={loading}
						>
							{loading ? "Placing..." : `Place ${formData.side} Order`}
						</button>
					</form>
				</div>

				{/* Open Positions */}
				<div className="card">
					<h2>Open Positions (Real-time)</h2>
					{Object.keys(positions).length === 0 ? (
						<div className="empty-state">
							<p>No open positions</p>
						</div>
					) : (
						<table className="table">
							<thead>
								<tr>
									<th>Symbol</th>
									<th>Net Quantity</th>
									<th>PnL</th>
								</tr>
							</thead>
							<tbody>
								{Object.entries(positions).map(([symbol, position]) => (
									<tr key={symbol}>
										<td>
											<strong>{symbol}</strong>
										</td>
										<td>{position.netQuantity}</td>
										<td className={position.pnl >= 0 ? "positive" : "negative"}>
											₹{formatCurrency(position.pnl)}
										</td>
									</tr>
								))}
							</tbody>
						</table>
					)}
				</div>
			</div>

			{/* Daily Analytics */}
			<div className="card">
				<h2>Today's Analytics</h2>
				{Object.keys(dailyAnalytics).length === 0 ? (
					<div className="empty-state">
						<p>No trades today</p>
					</div>
				) : (
					<table className="table">
						<thead>
							<tr>
								<th>Symbol</th>
								<th>Total Quantity</th>
								<th>Total PnL</th>
							</tr>
						</thead>
						<tbody>
							{Object.entries(dailyAnalytics).map(([symbol, analytics]) => (
								<tr key={symbol}>
									<td>
										<strong>{symbol}</strong>
									</td>
									<td>{analytics.totalQuantity}</td>
									<td
										className={
											analytics.totalPnl >= 0 ? "positive" : "negative"
										}
									>
										₹{formatCurrency(analytics.totalPnl)}
									</td>
								</tr>
							))}
						</tbody>
					</table>
				)}
			</div>

			{/* Trades Today */}
			<div className="card">
				<h2>Trades Today</h2>
				{tradesToday.length === 0 ? (
					<div className="empty-state">
						<p>No trades executed today</p>
					</div>
				) : (
					<table className="table">
						<thead>
							<tr>
								<th>Time</th>
								<th>Symbol</th>
								<th>Side</th>
								<th>Quantity</th>
								<th>Price</th>
							</tr>
						</thead>
						<tbody>
							{tradesToday.map((trade) => (
								<tr key={trade.tradeId}>
									<td>{formatDate(trade.tradeTime)}</td>
									<td>
										<strong>{trade.symbol}</strong>
									</td>
									<td
										className={trade.side === "BUY" ? "positive" : "negative"}
									>
										{trade.side}
									</td>
									<td>{trade.quantity}</td>
									<td>₹{formatCurrency(trade.price)}</td>
								</tr>
							))}
						</tbody>
					</table>
				)}
			</div>
		</div>
	);
}

export default App;
