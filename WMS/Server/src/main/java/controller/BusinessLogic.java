package controller;

import model.ProductDatabase;
import model.ProductEntry;

public class BusinessLogic {

    public double calculatePrice(String product, int quantity) {
        IPricingStrategy pricingStrategy = getPricingStrategy(product);
        double unitPrice = ProductDatabase.getInstance().getProductInfo(product).getUnitPrice();
        return pricingStrategy.calculateFinalPrice(unitPrice, quantity);
    }

    private IPricingStrategy getPricingStrategy(String product) {

        ProductEntry entry = ProductDatabase.getInstance().getProductInfo(product);
        int discountStrategyId = entry.getDiscountStrategyId();
        
        switch (discountStrategyId) {
            case 1:
                return new PricingStrategy001();
            case 2:
                return new PricingStrategy002();
            default:
                throw new IllegalArgumentException("No valid pricing strategy found for product: " + product);
        }
    }
}
