package service;

import Util.FileManager;
import entite.Custommer;
import entite.MDTE;
import entite.Options;
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

            List<String> lesOptions = new ArrayList<>();
            lesOptions.add("12");
            lesOptions.add("13");
            lesOptions.add("14");

            Order e = new Order(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2]), lesOptions, Float.parseFloat(values[3]), Integer.parseInt(values[4]), values[5]);
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

    public Custommer getCustommerByID(String id) {
        for (Custommer c : getCustommer()) {
            if (c.getID().equalsIgnoreCase(id)) {
                return c;
            }
        }
        return null;
    }

    public List<MDTE> getMDTEs(){
        List<MDTE> mdtes = new ArrayList<>();
        String filePath = "mdtes.txt";

        FileManager fm = new FileManager();
        String content = fm.read(filePath);
        String[] elements = content.split("\n");

        for (String element : elements) {
            String[] values = element.split(";");

            MDTE m = new MDTE(values[0], values[1], Float.parseFloat(values[2]));
            mdtes.add(m);
        }
        return mdtes;
    }

    public MDTE getMDTEByID(String id) {
        for (MDTE m : getMDTEs()) {
            if (m.getID().equalsIgnoreCase(id)) {
                return m;
            }
        }
        return null;
    }

    public List<Options> getOptions() {
        List<Options> options = new ArrayList<>();
        String filePath = "options.txt";

        FileManager fm = new FileManager();
        String content = fm.read(filePath);
        String[] elements = content.split("\n");

        for (String element : elements) {
            String[] values = element.split(";");

            Options o = new Options(values[0], values[1], values[2], Integer.parseInt(values[3]));
            options.add(o);
        }
        return options;
    }

    public Options getOptionByID(String id) {
        for (Options o : getOptions()) {
            if (o.getID().equalsIgnoreCase(id)) {
                return o;
            }
        }
        return null;
    }

}
