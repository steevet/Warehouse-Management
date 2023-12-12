package productFactoryModule;

import productState.IProductState;
import productState.LowState;

public class LowStateFactory implements ProductStateFactory {

	@Override
	public IProductState createState() {
		// TODO Auto-generated method stub
		return new LowState();
	}

}
