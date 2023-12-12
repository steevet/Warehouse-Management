package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ProductDatabase {
	
	private Connection connection;
	private String url = "jdbc:sqlite:products.db";
	
	private static ProductDatabase instance = null;
	
	private ProductDatabase() {
		// private constructor
	}
	
	public static ProductDatabase getInstance() {
		if (instance == null) {
			instance = new ProductDatabase();
		}
		return instance;
	}

	
	public HashMap<String, Integer> getAvailableProductsandQuantitiesInfo() {
		HashMap<String, Integer> info = new HashMap<String, Integer>();
		connection = null;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM items");

			while (result.next()) {
				info.put(result.getString("name"), Integer.parseInt(result.getString("current_stock")));
			}
			connection.close();

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}
	
	public HashMap<String, ProductEntry> getAvailableProductList() {
		HashMap<String, ProductEntry> availableProductList = new HashMap<String, ProductEntry>();
		connection = null;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM items");

			ProductEntry entry;
			while (result.next()) {
				entry = new ProductEntry();
				entry.setId(Integer.parseInt(result.getString("id")));
                entry.setName(result.getString("name"));
                entry.setCurrentStock(Integer.parseInt(result.getString("current_stock")));
                entry.setUnitPrice(Double.parseDouble(result.getString("unit_price")));
                entry.setTargetMaxStock(Integer.parseInt(result.getString("target_max_stock")));
                entry.setTargetMinStock(Integer.parseInt(result.getString("target_min_stock")));
                entry.setRestockSchedule(Integer.parseInt(result.getString("restock_schedule")));
                entry.setDiscountStrategyId(Integer.parseInt(result.getString("discount_strategy_id")));
                availableProductList.put(entry.getName(), entry);
			}
			connection.close();

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return availableProductList;
	}

	public ProductEntry getProductInfo(String productName) {
		ProductEntry entry = new ProductEntry();
		connection = null;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(url);
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM items WHERE name = ?");
			preparedStatement.setString(1, productName);
			ResultSet result = preparedStatement.executeQuery();
			
			if (result.next()) {
                entry.setId(Integer.parseInt(result.getString("id")));
                entry.setName(result.getString("name"));
                entry.setCurrentStock(Integer.parseInt(result.getString("current_stock")));
                entry.setUnitPrice(Double.parseDouble(result.getString("unit_price")));
                entry.setTargetMaxStock(Integer.parseInt(result.getString("target_max_stock")));
                entry.setTargetMinStock(Integer.parseInt(result.getString("target_min_stock")));
                entry.setRestockSchedule(Integer.parseInt(result.getString("restock_schedule")));
                entry.setDiscountStrategyId(Integer.parseInt(result.getString("discount_strategy_id")));
            }
			else {
				entry = null;
			}
			preparedStatement.close();
			result.close();
			connection.close();

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entry;
	}

	public void updateProductCurrentStockTo(String product, int currentStock) {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(url);
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE items SET current_stock = ? WHERE name = ?");
			preparedStatement.setInt(1, currentStock);
			preparedStatement.setString(2, product);
			preparedStatement.executeUpdate();
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	public LastOrder getLastOrder() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM orders ORDER BY id DESC LIMIT 1");
			
			if (result.next()) {
				LastOrder.setProduct(result.getString("product"));
				LastOrder.setQuantity(Integer.parseInt(result.getString("quantity")));
				LastOrder.setDate(LocalDateTime.parse(result.getString("timestamp")));
			}
			statement.close();
			result.close();
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
