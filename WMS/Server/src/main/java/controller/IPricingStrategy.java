package controller;

public interface IPricingStrategy {
	
	double calculateFinalPrice(double unitPrice, int quantity);

}
