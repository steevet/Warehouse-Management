package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ProductOrderingClientUI extends JFrame implements ActionListener {

    private static JComboBox<String> productList;
    private static JComboBox<Integer> quantityList;
    private static String theProduct;
    private static Integer theQuantity;
    private static String theTime;
    private JTextArea orderDetails;
    private static String productReport;
    private static String quantityReport;
    private static String timeReport;
    private static ProductOrderingClientUI instance = null;
    private Queue<Runnable> requestQueue = new LinkedList<>();
    private Thread requestProcessorThread;

    private ProductOrderingClientUI() {
        super("Product Ordering Client");

        JLabel step1 = new JLabel("Step1 Choose Product");
        JLabel step2 = new JLabel("Step2 Choose Quantity");

        JLabel chooseProductLabel = new JLabel(": ");
        Vector<String> productNames = new Vector<String>();
        productNames.add("Laptop");
        productNames.add("Table");
        productNames.add("Lamp");
        productNames.add("Chair");
        productNames.add("Soccer Ball");
        productList = new JComboBox<String>(productNames);

        JLabel chooseQuantityLabel = new JLabel(": ");
        Vector<Integer> quantity = new Vector<Integer>();
        for (int i = 1; i <= 100; i++) {
            quantity.add(i);
        }
        quantityList = new JComboBox<Integer>(quantity);
        JButton addQuantity = new JButton("Choose");
        addQuantity.setActionCommand("addQuantity");
        addQuantity.addActionListener(this);
        quantityList.setActionCommand("addQuantity");

        JPanel north = new JPanel();
        north.add(step1);
        north.add(chooseProductLabel);
        north.add(productList);
        north.add(step2);
        north.add(chooseQuantityLabel);
        north.add(quantityList);
        north.add(addQuantity);

        JPanel east = new JPanel();

        JPanel west = new JPanel();
        west.setLayout(new GridLayout(2, 0));

        JLabel orderDetailsLabel = new JLabel("Order Details: ");
        orderDetails = new JTextArea(30, 60);
        JScrollPane orderDetailsScrollPane = new JScrollPane(orderDetails);
        east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
        east.add(orderDetailsLabel);
        east.add(orderDetailsScrollPane);

        getContentPane().add(north, BorderLayout.NORTH);
        getContentPane().add(east, BorderLayout.EAST);
        getContentPane().add(west, BorderLayout.WEST);

        requestProcessorThread = new Thread(this::processRequestQueue);
        requestProcessorThread.start();
    }

    public static ProductOrderingClientUI getInstance() {
        if (instance == null) {
            instance = new ProductOrderingClientUI();
        }
        return instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {        
        theProduct = productList.getSelectedItem().toString();
        theQuantity = (Integer) quantityList.getSelectedItem();
        theTime = LocalDateTime.now().toString();
        
        if (theProduct != null && theQuantity != null) {
            productReport = "Product : " + theProduct + "\n";
            quantityReport = "Quantity : " + theQuantity + "\n";
            timeReport = "Client Time Stamp : " +  theTime + "\n";
            orderDetails.append(productReport + quantityReport + timeReport + "\n");

            synchronized (requestQueue) {
                requestQueue.add(() -> {
                    String request = Client.getInstance().request(theProduct, theQuantity, theTime);
                    orderDetails.append(request + "\n\n");
                });
                requestQueue.notify();
            }
        }
    }

    private void processRequestQueue() {
        while (true) {
            Runnable requestTask;
            synchronized (requestQueue) {
                while (requestQueue.isEmpty()) {
                    try {
                        requestQueue.wait();
                    } catch (InterruptedException e) {
                        // Handle interruption
                    }
                }
                requestTask = requestQueue.poll();
            }
            requestTask.run();
        }
    }

    public static void main(String[] args) {        
        JFrame frame = ProductOrderingClientUI.getInstance();
        frame.setSize(900, 600);
        frame.setVisible(true);
    }
}
