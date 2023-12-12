package model;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class LastOrder {

    private static LastOrder instance = null;

    private static String product;
    private static int quantity;
    private static LocalDateTime date;

    private Timer timer;

    public LastOrder() {
        scheduleLastOrderRefresh();
    }

    public static synchronized LastOrder getInstance() {
        if (instance == null) {
            instance = new LastOrder();
        }
        return instance;
    }

    private void scheduleLastOrderRefresh() {
        timer = new Timer();
        TimerTask refreshTask = new TimerTask() {
            @Override
            public void run() {
                findLastOrder();
            }
        };

        long period = 1000;
        timer.scheduleAtFixedRate(refreshTask, 0, period);
    }

    public void findLastOrder() {
        LastOrder latestOrder = ProductDatabase.getInstance().getLastOrder();
        if (latestOrder != null) {
            synchronized(LastOrder.class) {
                product = latestOrder.getProduct();
                quantity = latestOrder.getQuantity();
                date = latestOrder.getDate();
            }
        }
    }

    public static String getProduct() {
        return product;
    }

    public static void setProduct(String product) {
        synchronized(LastOrder.class) {
            LastOrder.product = product;
        }
    }

    public static int getQuantity() {
        return quantity;
    }

    public static void setQuantity(int quantity) {
        synchronized(LastOrder.class) {
            LastOrder.quantity = quantity;
        }
    }

    public static LocalDateTime getDate() {
        return date;
    }

    public static void setDate(LocalDateTime date) {
        synchronized(LastOrder.class) {
            LastOrder.date = date;
        }
    }
}
