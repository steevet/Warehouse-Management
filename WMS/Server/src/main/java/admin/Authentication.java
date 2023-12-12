package admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Authentication implements IAuthentication {

	public boolean authenticate(String username, String password) {
		boolean authenticate = false;
		Connection connection = null;
		try {
			Class.forName("org.sqlite.JDBC");
			String url = "jdbc:sqlite:credentials.db";
			connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM admins");
			
			while(result.next()) {
				if (result.getString("username").equals(username) && result.getString("password").equals(password)) {
					authenticate = true;
					break;
				}
			}
			connection.close();
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return authenticate;
	}

}
