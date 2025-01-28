package Modele;

import Services.DBManager;
import Services.FileManager;
import entite.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Modele {

    private final FileManager fileManager = new FileManager();
    private final DBManager dbManager = new DBManager();

    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        String filePath = "orders.txt";
        String content = fileManager.read(filePath, false);

        for (String line : content.split("\n")) {
            String[] values = line.split(";");
            List<String> options = getOptionsByOrder(values[3]);

            Order order = new Order(
                    values[0],
                    Integer.parseInt(values[1]),
                    Integer.parseInt(values[2]),
                    options,
                    Float.parseFloat(values[4]),
                    Integer.parseInt(values[5]),
                    values[6]
            );
            orders.add(order);
        }
        return orders;
    }

    public Order getOrderByID(String id) {
        return getOrders().stream()
                .filter(order -> order.getID().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public List<Custommer> getCustommers() {
        List<Custommer> custommers = new ArrayList<>();
        String filePath = "custommers.txt";
        String content = fileManager.read(filePath, true);

        for (String line : content.split("\n")) {
            String[] values = line.split(";");
            Custommer custommer = new Custommer(values[0], values[1], values[2], values[3], values[4]);
            custommers.add(custommer);
        }

        return custommers;
    }

    public Custommer getCustommerByID(String id) {
        return getCustommers().stream()
                .filter(custommer -> custommer.getID().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public List<MDTE> getMDTEs() throws Exception {
        List<MDTE> mdtes = new ArrayList<>();
        String filePath = "mdtes.txt";
        String content = fileManager.read(filePath, false);

        for (String line : content.split("\n")) {
            String[] values = line.split(";");
            MDTE mdte = new MDTE(values[0], values[1], Float.parseFloat(values[2]));
            mdtes.add(mdte);
        }

        return mdtes;
    }

    public MDTE getMDTEByID(String id) throws Exception {
        return getMDTEs().stream()
                .filter(mdte -> mdte.getID().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public List<Options> getOptions() throws Exception {
        List<Options> options = new ArrayList<>();
        String filePath = "options.txt";
        String content = fileManager.read(filePath, false);

        for (String line : content.split("\n")) {
            String[] values = line.split(";");
            Options option = new Options(
                    values[0], values[1], values[2], Integer.parseInt(values[3])
            );
            options.add(option);
        }

        return options;
    }

    public Options getOptionByID(String id) throws Exception {
        return getOptions().stream()
                .filter(option -> option.getID().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public static List<String> getOptionsByOrder(String input) {
        if (input == null || input.length() < 2 || !input.startsWith("[") || !input.endsWith("]")) {
            throw new IllegalArgumentException("Format non valide");
        }

        String trimmedInput = input.substring(1, input.length() - 1);
        List<String> result = new ArrayList<>();

        for (String part : trimmedInput.split(",")) {
            result.add(part.trim());
        }

        return result;
    }

    public List<Stock> getStocks() throws Exception {
        List<Stock> stocks = new ArrayList<>();
        String filePath = "stocks.txt";
        String content = fileManager.read(filePath, false);

        for (String line : content.split("\n")) {
            String[] values = line.split(";");
            Stock stock = new Stock(
                    values[0], values[1], values[2], Integer.parseInt(values[3])
            );
            stocks.add(stock);
        }

        return stocks;
    }

    public Stock getStockByRefOption(String refOpt) throws Exception {
        return getStocks().stream()
                .filter(stock -> stock.getIDOption().equals(refOpt))
                .findFirst()
                .orElse(null);
    }

    public void validateOrder(Order order, Integer qteStock1, Integer qteStock2, Integer qteStock3) {
        try {
            List<Stock> stocks = new ArrayList<>();

            for (String option : order.getOptions()) {
                Stock s = getStockByRefOption(option);
                stocks.add(s);
            }

            List<Integer> qtes = Arrays.asList(qteStock1, qteStock2, qteStock3);

            int i = 0;
            for (Stock stock : stocks) {
                Stock updatedStock = new Stock(stock.getID(), stock.getIDOption(), stock.getIDRack(), (stock.getQuantity() - qtes.get(i)));
                i += 1;
                fileManager.write("stocks.txt", updatedStock.toString());
            }

            Order updatedOrder = new Order(order.getID(), order.getClientID(), order.getMdteID(), order.getOptions(), order.getTotalPrice(), 6, order.getTrackingNumber());
            fileManager.write("orders.txt", updatedOrder.toString());

        } catch (Exception e) {
            throw new IllegalArgumentException("Une erreur est survenue lors de la validation de la commande");
        }
    }
}
