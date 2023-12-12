package middleware;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.sun.net.httpserver.HttpExchange;
import controller.Order;

public class Consumer implements Runnable {

    private final BlockingQueue<HttpExchange> queue;

    public Consumer(BlockingQueue<HttpExchange> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                HttpExchange exchange = queue.take(); // Blocks if queue is empty
                Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());

                String product = params.get("product");
                Integer quantity = safeParseInt(params.get("quantity"));
                LocalDateTime timestamp = null;
                if (params.get("timestamp") != null) {
                    timestamp = LocalDateTime.parse(params.get("timestamp"));
                }

                if (product != null && quantity != null && timestamp != null) {
                    Order order = new Order(product, quantity, timestamp);
                    String response = order.processOrder();

                    if (response.equals(Order.EXCEEDS_AVAILABLE_STOCK)) {
                        sendResponse(exchange, "Please order less!", HttpURLConnection.HTTP_BAD_REQUEST);
                    } else {
                        sendResponse(exchange, response, 200);
                    }
                } else {
                    sendResponse(exchange, "Invalid request parameters", HttpURLConnection.HTTP_BAD_REQUEST);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private Integer safeParseInt(String str) {
        try {
            return str != null ? Integer.parseInt(str) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
