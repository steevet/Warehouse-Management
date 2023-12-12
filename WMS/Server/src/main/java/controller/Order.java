package controller;

import java.time.LocalDateTime;
import model.ProductDatabase;
import model.ProductEntry;

public class Order {
    public static final String EXCEEDS_AVAILABLE_STOCK = "Order exceeds available stock";

    private String product;
    private int quantity;
    private LocalDateTime timestamp;

    public Order(String product, int quantity, LocalDateTime timestamp) {
        this.product = product;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public String processOrder() {
        ProductEntry entry = ProductDatabase.getInstance().getProductInfo(product);
        if (entry == null) {
            return "Product not found";
        }

        // Check if ordered quantity exceeds the current stock
        if (quantity > entry.getCurrentStock()) {
            return EXCEEDS_AVAILABLE_STOCK; // Specific response for this case
        } else if (quantity > entry.getTargetMaxStock()) {
            return "Order exceeds max quantity and cannot be processed";
        } else {
            // Process the order
            double finalPrice = new BusinessLogic().calculatePrice(product, quantity);
            ProductDatabase.getInstance().updateProductCurrentStockTo(product, entry.getCurrentStock() - quantity);
            return "Order finalized with total price $" + finalPrice;
        }
    }
}
