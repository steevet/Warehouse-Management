package admin;

public class AuthenticationProxy implements IAuthentication {
	
	private Authentication auth;
	
	public boolean authenticate(String username, String password) {
		if (auth == null) {
			auth = new Authentication();
		}
		return auth.authenticate(username, password);
	}

}
