package model;

import java.util.HashMap;

public class AvailableProductList {
    
    private HashMap<String, Integer> availableProductandQuantitiesList;
    private HashMap<String, ProductEntry> availableProductList;

    private static AvailableProductList instance = null;
    
    private AvailableProductList() {
        availableProductandQuantitiesList = new HashMap<>();
        availableProductList = new HashMap<>();
    }
    
    public static synchronized AvailableProductList getInstance() {
        if (instance == null) {
            instance = new AvailableProductList();
        }
        return instance;
    }

    public HashMap<String, Integer> findAvailableProductsAndQuantities() {
        availableProductandQuantitiesList = ProductDatabase.getInstance().getAvailableProductsandQuantitiesInfo();
        return new HashMap<>(availableProductandQuantitiesList); // Return a copy to prevent external modification
    }
    
    public HashMap<String, ProductEntry> getAvailableProductList() {
        availableProductList = ProductDatabase.getInstance().getAvailableProductList();
        return new HashMap<>(availableProductList); // Return a copy to prevent external modification
    }

}
