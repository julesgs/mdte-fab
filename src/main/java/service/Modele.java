package service;

import Util.FileManager;
import entite.Order;

import java.util.ArrayList;
import java.util.List;

public class Modele {

    public List<Order> getOrder() {


        List<Order> orders = new ArrayList<>();
        String filePath = "orders.txt";

        FileManager fm = new FileManager();

        String content = fm.read(filePath);

        String[] elements = content.split("\n");

        for (String element : elements) {
            String[] values = element.split(";");

            Order e = new Order(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2]), Float.parseFloat(values[3]), Integer.parseInt(values[4]), values[5]);
            orders.add(e);
        }

        return orders;
    }

}
