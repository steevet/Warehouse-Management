package controller;

import model.ProductDatabase;

public class PricingStrategy001 implements IPricingStrategy {

	private double basePrice = 0;
	private static final double DISCOUNT_THRESHOLD_QUANTITY = 100; // x
	private static final double DISCOUNT_PERCENTAGE = 0.1; // y
	private static final double ADDITIONAL_DISCOUNT_THRESHOLD = 1000;
	private static final double ADDITIONAL_DISCOUNT_PERCENTAGE = 0.05;

	@Override
	public double calculateFinalPrice(double unitPrice, int quantity) {
		this.basePrice = unitPrice * quantity;
		// Apply quantity-based discount
		if (quantity > DISCOUNT_THRESHOLD_QUANTITY) {
			basePrice -= basePrice * DISCOUNT_PERCENTAGE;
		}

		// Apply additional discount based on total order value
		if (unitPrice > ADDITIONAL_DISCOUNT_THRESHOLD) {
			basePrice -= basePrice * ADDITIONAL_DISCOUNT_PERCENTAGE;
		}
		
		basePrice *= quantity;

		return basePrice;
	}

}
