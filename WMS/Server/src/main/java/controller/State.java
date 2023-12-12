package controller;

import model.ProductEntry;

public interface State {
	
	void handle(ProductEntry entry);

}
