package controller;

import java.util.Map;

import model.AvailableProductList;
import model.ProductDatabase;
import model.ProductEntry;

public class RestockingToFulfillOrder implements State {
	 private static final int LOW_STOCK_THRESHOLD = 15;
	 private static final int RESTOCK_QUANTITY = 50;
	@Override
	public void handle(ProductEntry entry) {
		entry.setState(new RestockingFulfilled());
		
	}

		    public void checkAndRestockProducts() {
		        Map<String, ProductEntry> productEntries = AvailableProductList.getInstance().getAvailableProductList();
		        
		        for (Map.Entry<String, ProductEntry> entry : productEntries.entrySet()) {
		            ProductEntry product = entry.getValue();
		            
		            if (product.getCurrentStock() < LOW_STOCK_THRESHOLD) {
		                restockProduct(product, RESTOCK_QUANTITY);
		            }
		        }
		    }

		    private void restockProduct(ProductEntry product, int restockQuantity) {
		        System.out.println("Restocking " + product.getName() + " to " + restockQuantity + " units.");
		        product.setCurrentStock(restockQuantity);
		        ProductDatabase.getInstance().updateProductCurrentStockTo(product.getName(), restockQuantity);
		    }
		}
	

