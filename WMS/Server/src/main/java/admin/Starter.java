package admin;

import javax.swing.JFrame;

import frontend.Server;
import model.ProductDatabase;
import viewer.WarehouseServerUI;

public class Starter {
	
	
	public static void start(String username, String password) {
		// authentication
		AuthenticationProxy proxy = new AuthenticationProxy();
		boolean authentication = proxy.authenticate(username, password);
		if (authentication == true) {
			try {
				// start java http server and allow connections with client processes
				Server httpServer = new Server();
				httpServer.start();
				
				// display server UI
				JFrame frame = WarehouseServerUI.getInstance();
				frame.setSize(800, 600);
				frame.setVisible(true);
				
				// connect to product database
				ProductDatabase.getInstance();				
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
			System.exit(0);
		}
	}

}
