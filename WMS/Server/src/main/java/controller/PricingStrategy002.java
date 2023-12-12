package controller;

public class PricingStrategy002 implements IPricingStrategy {
	
	private double basePrice = 0;
	private static final double SPECIAL_OFFER_THRESHOLD_QUANTITY = 20;
	private static final double SPECIAL_OFFER_PERCENTAGE = 0.15;

	public double calculateFinalPrice(double unitPrice, int quantity) {
		this.basePrice = unitPrice * quantity;
		// Apply special offer for large quantities
		if (quantity > SPECIAL_OFFER_THRESHOLD_QUANTITY) {
			basePrice -= basePrice * SPECIAL_OFFER_PERCENTAGE;
		}
		return basePrice;
	}

}
