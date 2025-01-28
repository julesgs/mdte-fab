package Services;

import java.sql.*;

public class DBManager {

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

    // Fonction pour ajouter un Custommer
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

    // Fonction pour ajouter un MDTE
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

    // Fonction pour ajouter une Option
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

    // Fonction pour ajouter une commande (Order)
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

    // Fonction pour ajouter un Stock
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