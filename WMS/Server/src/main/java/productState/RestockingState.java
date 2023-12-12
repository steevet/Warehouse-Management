package productState;

public class RestockingState implements IProductState {

	@Override
	public void doSomething() {
		
		System.out.println("Product: Restocking State");

	}

}
