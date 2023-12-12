package productFactoryModule;

import productState.IProductState;
import productState.RestockingState;

public class RestockingStateFactory implements ProductStateFactory {

	@Override
	public IProductState createState() {
		// TODO Auto-generated method stub
		return new RestockingState();
	}

}
