package Helpers;

import AlgorithmObjects.Shared.Order;
import au.com.bytecode.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WalmartCSVReader {

    public static List<Order> GetOrders() throws IOException {
        CSVReader reader = new CSVReader(new FileReader("./in/walmart_sorted.csv"));
        String[] nextLine;
        reader.readNext(); // header

        List<Order> orders = new ArrayList<Order>();
        Order currentOrder = new Order();

        String previousSubCategory = "";
        while ((nextLine = reader.readNext()) != null) {
            // 1 - Order ID
            // 18 - Product Sub-Category
            // 19 - Product Name
            // System.out.println(nextLine[1] + " " + nextLine[19]);

            String orderID = nextLine[1];
            String productSubCategory = nextLine[18];
            if (!orderID.equalsIgnoreCase(currentOrder.getOrderID())) {
                if (currentOrder.getOrderID().length() > 0) {
                    previousSubCategory = "";
                    orders.add(currentOrder);
                }
                currentOrder = new Order();
                currentOrder.setOrderID(orderID);
            }

            // Add the product to the item set if it wasn't just added
            if (!previousSubCategory.equalsIgnoreCase(productSubCategory)) {
                currentOrder.getItemSet().add(productSubCategory);
            }
            previousSubCategory = productSubCategory;
        }

        return orders;
    }


    public static void main(String[] args) {

    }

}
