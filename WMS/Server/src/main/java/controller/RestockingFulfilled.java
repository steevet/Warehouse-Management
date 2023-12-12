package controller;

import model.ProductEntry;

public class RestockingFulfilled implements State {

	@Override
	public void handle(ProductEntry entry) {
		entry.setState(new RestockingToFulfillOrder());
	}

}
