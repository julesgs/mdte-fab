package service;

import Util.FileManager;
import entite.Custommer;
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

    public Order getOrderByID(String id) {
        for (Order o : getOrder()) {
            if (o.getID().equalsIgnoreCase(id)) {
                return o;
            }
        }
        return null;
    }

    public List<Custommer> getCustommer() {
        List<Custommer> custommers = new ArrayList<>();

        String filePath = "custommers.txt";

        FileManager fm = new FileManager();
        String content = fm.read(filePath);
        String[] elements = content.split("\n");

        for (String element : elements) {
            String[] values = element.split(";");

            Custommer c = new Custommer(values[0], values[1], values[2], values[3], values[4]);
            custommers.add(c);
        }

        return custommers;
    }

    public String getCustommerByID(String id) {
        for (Custommer c : getCustommer()) {
            if (c.getID().equalsIgnoreCase(id)) {
                return c.getFirstName() + " " + c.getLastName();
            }
        }
        return null;
    }

}
