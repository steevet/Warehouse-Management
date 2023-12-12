package productFactoryModule;

import java.util.HashMap;

import productState.IProductState;

public class ProductStateFactoryRepo {

	private HashMap<String, ProductStateFactory> repo = new HashMap<>();

	public void addFactory(String state, ProductStateFactory factory) {

		repo.put(state, factory);

	}

	public ProductStateFactory getFactory(String State) {
		
		return repo.get(State);
		
		
	}
	
	
	public IProductState getProductState(String state) {

		ProductStateFactory factory = repo.get(state);
		if (factory != null) {

			return factory.createState();

		}

		return null;

	}

	public HashMap<String, ProductStateFactory> getRepo() {
		return repo;
	}

	public void setRepo(HashMap<String, ProductStateFactory> repo) {
		this.repo = repo;
	}

}
