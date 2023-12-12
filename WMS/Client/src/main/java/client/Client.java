package client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Client {

    private static Client instance = null;

    private Client() {
    }

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public String request(String product, Integer quantity, String timestamp) {
        String urlString = String.format("http://localhost:8000/makeorder?product=%s&quantity=%d&timestamp=%s", product, quantity, timestamp);
        int maxRetries = 3;
        int attempts = 0;

        while (attempts < maxRetries) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                int responsecode = conn.getResponseCode();
                if (responsecode == 200) {
                    Scanner sc = new Scanner(url.openStream());
                    StringBuilder inline = new StringBuilder();
                    while (sc.hasNext()) {
                        inline.append(sc.nextLine());
                    }
                    sc.close();

                    return inline.toString();
                }

            } catch (IOException e) {
                attempts++;
                System.out.println("Attempt " + attempts + " failed. Retrying...");
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // Restore interrupt status
                }
            }
        }
        return "Request failed after " + maxRetries + " attempts.";
    }
}
