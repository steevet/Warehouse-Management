package viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import model.AvailableProductList;
import model.LastOrder;
import model.ProductEntry;

public class WarehouseServerUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private Map<String, Integer> productData;
    private Map<String, Integer> previousProductData = new HashMap<>();
    private LastOrder theLastOrder;
    private JTextArea lastOrderInfo;
    private JTextArea report; 
    private ChartPanel chartPanel; 
    

    private static WarehouseServerUI instance = null;

    public static synchronized WarehouseServerUI getInstance() {
        if (instance == null) {
            instance = new WarehouseServerUI();
        }
        return instance;
    }

    private WarehouseServerUI() {
        super("Warehouse Server UI");

        productData = AvailableProductList.getInstance().findAvailableProductsAndQuantities();
        theLastOrder = LastOrder.getInstance();
        theLastOrder.findLastOrder();

        JPanel west = new JPanel(new GridLayout(1, 0));
        JPanel east = new JPanel(new GridLayout(1, 0));

        getContentPane().add(west, BorderLayout.WEST);
        getContentPane().add(east, BorderLayout.EAST);

        createCharts(west);
        createReport(east);
        createLastOrderInfo();
        startDataRefreshTimer();
    }

    private void createCharts(JPanel west) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : productData.entrySet()) {
            dataset.setValue(entry.getValue(), entry.getKey(), "");
        }

        CategoryPlot plot = new CategoryPlot();
        BarRenderer barrenderer = new BarRenderer();

        plot.setDataset(0, dataset);
        plot.setRenderer(0, barrenderer);
        CategoryAxis domainAxis = new CategoryAxis("");
        plot.setDomainAxis(domainAxis);
        plot.setRangeAxis(new NumberAxis(""));

        JFreeChart barChart = new JFreeChart("Warehouse Product Monitor System",
                                             new Font("Serif", Font.BOLD, 18), plot, true);
        chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        west.add(chartPanel);
    }

    private void createReport(JPanel east) {
        report = new JTextArea();
        report.setEditable(false);
        report.setPreferredSize(new Dimension(400, 300));
        report.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        report.setBackground(Color.white);

        updateProductReport(productData);
        JScrollPane outputScrollPane = new JScrollPane(report);
        east.add(outputScrollPane);
    }

    private void createLastOrderInfo() {
        lastOrderInfo = new JTextArea();
        lastOrderInfo.setEditable(false);
        lastOrderInfo.setPreferredSize(new Dimension(400, 300));
        lastOrderInfo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        lastOrderInfo.setBackground(Color.white);

        updateLastOrderInfo(productData);
        JScrollPane outputScrollPane = new JScrollPane(lastOrderInfo);
        getContentPane().add(outputScrollPane, BorderLayout.SOUTH);
    }

    private void startDataRefreshTimer() {
        Timer timer = new Timer(3000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
        timer.start();
    }

    private void refreshData() {
        Map<String, ProductEntry> productEntries = AvailableProductList.getInstance().getAvailableProductList();
        Map<String, Integer> newProductData = new HashMap<>();
        boolean dataChanged = false;

        for (Map.Entry<String, ProductEntry> entry : productEntries.entrySet()) {
            String productName = entry.getKey();
            int newQuantity = entry.getValue().getCurrentStock();
            newProductData.put(productName, newQuantity);

            int previousQuantity = previousProductData.getOrDefault(productName, -1);
            if (previousQuantity != newQuantity) {
                dataChanged = true;
            }
        }

        if (dataChanged) {
            updateProductReport(newProductData);
            updateLastOrderInfo(newProductData);
            updateBarChart(newProductData);

            previousProductData = new HashMap<>(newProductData);
        }
    }

    
    
    private int calculateChange(int previousQuantity, int newQuantity) {
        double tempChange = previousQuantity - newQuantity;
        if (tempChange % 2 == 0) {
            return (int) (tempChange / 2);
        } else {
            return (int) ((tempChange / 2) + 0.5);
        }
    }



    private void updateProductReport(Map<String, Integer> newProductData) {
        StringBuilder reportMessage = new StringBuilder("Current Product Quantity in Warehouse\n" + 
                                                       "==============================\n");
;
		for (Map.Entry<String, Integer> entry : newProductData.entrySet()) {
            String productName = entry.getKey();

            int newQuantity = entry.getValue();
            int previousQuantity = previousProductData.getOrDefault(productName, newQuantity);

            int change =  calculateChange( previousQuantity,  newQuantity);

             newQuantity +=change;

             previousQuantity = previousProductData.getOrDefault(productName, newQuantity);


            reportMessage.append(entry.getKey())
                         .append("\n \t Quantity ==> ")
                         .append(newQuantity)
                         .append("\n");
        
		}
        report.setText(reportMessage.toString());
    }
    

    private void updateLastOrderInfo(Map<String, Integer> newProductData) {
        StringBuilder reportMessage = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        boolean changesDetected = false;
		for (Map.Entry<String, Integer> entry : newProductData.entrySet()) {
            String productName = entry.getKey();
            int newQuantity = entry.getValue();
            int previousQuantity = previousProductData.getOrDefault(productName, newQuantity);
            int change = calculateChange(previousQuantity, newQuantity);

            
            if (change > 0) {
                reportMessage.append(" Client Ordered ")
                             .append(change)
                             .append(" units ")
                             .append(productName)
                			 .append("\n")
                			 .append("Time: ")
                			 .append(formattedDateTime)
                			 .append("\n\n");

                changesDetected = true;
            }
        }

        if (changesDetected) {
            lastOrderInfo.append(reportMessage.toString());
        }
    }


    private void updateBarChart(Map<String, Integer> newProductData) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : newProductData.entrySet()) {

            String productName = entry.getKey();
            int newQuantity = entry.getValue();
            int previousQuantity = previousProductData.getOrDefault(productName, newQuantity);
            int change = calculateChange(previousQuantity, newQuantity);
            newQuantity +=change;
             previousQuantity = previousProductData.getOrDefault(productName, newQuantity);



            dataset.setValue(newQuantity, entry.getKey(), "");
            

        if (chartPanel != null && chartPanel.getChart() != null) {
            chartPanel.getChart().getCategoryPlot().setDataset(dataset);
        }
    }

}
}
