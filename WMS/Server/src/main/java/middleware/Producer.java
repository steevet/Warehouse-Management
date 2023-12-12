package middleware;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Producer implements HttpHandler {

	private final BlockingQueue<HttpExchange> queue;

	public Producer(BlockingQueue<HttpExchange> queue) {
		this.queue = queue;
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			queue.put(exchange);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}