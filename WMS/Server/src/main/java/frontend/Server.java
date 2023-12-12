package frontend;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import middleware.Consumer;
import middleware.Producer;

public class Server {

    public void start() throws Exception {        
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        
        BlockingQueue<HttpExchange> queue = new LinkedBlockingQueue<>();
        server.createContext("/makeorder", new Producer(queue));
        
        
        Thread consumer = new Thread(new Consumer(queue));
        consumer.start();
        
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }
}
