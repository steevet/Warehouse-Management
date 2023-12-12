package model;

import controller.State;

public class ProductEntry {
	
	private int id;
	private String name;
	private int currentStock;
	private Double unitPrice;
	private int targetMaxStock;
	private int targetMinStock;
	private int restockSchedule;
	private int discountStrategyId;
	
	private State state;
	
	public ProductEntry() {
		
	}
	
	public ProductEntry(State status) {
		this.state = status;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public State getState() {
		return state;
	}
	
	public void request() {
		state.handle(this);
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getCurrentStock() {
		return currentStock;
	}
	
	public Double getUnitPrice() {
		return unitPrice;
	}
	
	public int getTargetMaxStock() {
		return targetMaxStock;
	}
	
	public int getTargetMinStock() {
		return targetMinStock;
	}
	
	public int getRestockSchedule() {
		return restockSchedule;
	}
	
	public int getDiscountStrategyId() {
		return discountStrategyId;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setCurrentStock(int currentStock) {
		this.currentStock = currentStock;
	}
	
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public void setTargetMaxStock(int targetMaxStock) {
		this.targetMaxStock = targetMaxStock;
	}
	
	public void setTargetMinStock(int targetMinStock) {
		this.targetMinStock = targetMinStock;
	}
	
	public void setRestockSchedule(int restockSchedule) {
		this.restockSchedule = restockSchedule;
	}
	
	public void setDiscountStrategyId(int discountStrategyId) {
		this.discountStrategyId = discountStrategyId;
	}

}
