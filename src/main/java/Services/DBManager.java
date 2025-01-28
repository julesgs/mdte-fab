package Services;

import entite.Custommer;
import entite.MDTE;
import entite.Options;
import entite.Stock;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private final FileManager fileManager = new FileManager();

    // ========================== CONNEXION À LA BASE DE DONNÉES ==========================

    public Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:8889/fab";
        String user = "root";
        String password = "root";

        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new Exception("Connexion BDD : Le driver n'a pas été trouvé");
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la connexion à la base de données.");
        }

        return connection;
    }

    // ========================== GESTION DES CUSTOMMERS ==========================

    // Ajouter un Custommer
    public void addCustommer(String id, String firstName, String lastName, String email, String adress) {
        String sql = "INSERT INTO Custommer (_id, _firstName, _lastName, _email, _adress) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, email);
            pstmt.setString(5, adress);

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Importer les Custommers dans la base de données
    public void getCustommersForBDD() {
        List<Custommer> custommers = new ArrayList<>();
        String filePath = "custommers.txt";
        String content = fileManager.read(filePath);

        for (String line : content.split("\n")) {
            String[] values = line.split(";");

            Custommer custommer = new Custommer(values[0], values[1], values[2], values[3], values[4]);
            custommers.add(custommer);
        }

        for (Custommer c : custommers) {
            this.addCustommer(c.getID(), c.getFirstName(), c.getLastName(), c.getEmail(), c.getAdress());
        }
    }

    // ========================== GESTION DES MDTEs ==========================

    // Ajouter un MDTE
    public void addMDTE(String id, String name, float price) {
        String sql = "INSERT INTO MDTE (_id, _name, _price) VALUES (?, ?, ?)";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setFloat(3, price);

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Importer les MDTEs dans la base de données
    public void getMDTEsForBDD() throws Exception {
        List<MDTE> mdtes = new ArrayList<>();
        String filePath = "mdtes.txt";
        String content = fileManager.read(filePath);

        for (String line : content.split("\n")) {
            String[] values = line.split(";");

            MDTE mdte = new MDTE(values[0], values[1], Float.parseFloat(values[2]));
            mdtes.add(mdte);
        }

        try {
            for (MDTE m : mdtes) {
                this.addMDTE(m.getID(), m.getName(), m.getPrice());
            }
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'envoi des MDTE à la base de données");
        }
    }

    // ========================== GESTION DES OPTIONS ==========================

    // Ajouter une Option
    public void addOption(String id, String name, String type, String mdteID) {
        String sql = "INSERT INTO Options (_id, _name, _type, _mdteID) VALUES (?, ?, ?, ?)";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, type);
            pstmt.setString(4, mdteID);

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Importer les Options dans la base de données
    public void getOptionsForBDD() throws Exception {
        List<Options> options = new ArrayList<>();
        String filePath = "options.txt";
        String content = fileManager.read(filePath);

        for (String line : content.split("\n")) {
            String[] values = line.split(";");

            Options option = new Options(
                    values[0], values[1], values[2], Integer.parseInt(values[3])
            );
            options.add(option);
        }

        try {
            for (Options o : options) {
                this.addOption(o.getID(), o.getName(), o.getType(), o.getMdteID().toString());
            }
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'envoi des options à la base de données");
        }
    }

    // ========================== GESTION DES STOCKS ==========================

    // Ajouter un Stock
    public void addStock(String id, String idOption, String idRack, int quantity) {
        String sql = "INSERT INTO Stock (_id, _idOption, _idRack, _quantity) VALUES (?, ?, ?, ?)";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.setString(2, idOption);
            pstmt.setString(3, idRack);
            pstmt.setInt(4, quantity);

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Importer les Stocks dans la base de données
    public void getStocksForBDD() throws Exception {
        List<Stock> stocks = new ArrayList<>();
        String filePath = "stocks.txt";
        String content = fileManager.read(filePath);

        for (String line : content.split("\n")) {
            String[] values = line.split(";");

            Stock stock = new Stock(
                    values[0], values[1], values[2], Integer.parseInt(values[3])
            );
            stocks.add(stock);
        }

        for (Stock s : stocks) {
            this.addStock(s.getID(), s.getIDOption(), s.getIDRack(), s.getQuantity());
        }
    }

    // ========================== GESTION DES COMMANDES ==========================

    // Ajouter une commande (Order)
    public void addOrder(String id, String clientId, String mdteId, float totalPrice, int state, String trackingNumber) {
        String sql = "INSERT INTO `Orders` (_id, _clientId, _mdteId, _totalPrice, _state, _trackingNumber) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.setString(2, clientId);
            pstmt.setString(3, mdteId);
            pstmt.setFloat(4, totalPrice);
            pstmt.setInt(5, state);
            pstmt.setString(6, trackingNumber);

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========================== VIDAGE DES TABLES ==========================

    // Vider toutes les tables
    public void vidage() {
        String[] tables = {"Custommer", "Options", "Orders", "Stock", "MDTE"};

        try (Connection conn = this.getConnection()) {
            Statement stmt = conn.createStatement();

            for (String table : tables) {
                String query = "TRUNCATE TABLE " + table;
                stmt.executeUpdate(query);
            }
            System.out.println("Toutes les tables ont été vidées");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
