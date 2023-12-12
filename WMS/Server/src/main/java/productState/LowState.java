package productState;

public class LowState implements IProductState {

	@Override
	public void doSomething() {

		System.out.println("Restocking Operation for Product X initiated");

	}

}
