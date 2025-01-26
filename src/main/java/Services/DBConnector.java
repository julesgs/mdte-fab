package Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    public Connection getConnection() {
        String url = "jdbc:mysql://localhost:8889/fab";
        String user = "root";
        String password = "root";

        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie à la base de données !");
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur : Driver MySQL introuvable.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données.");
            e.printStackTrace();
        }

        return connection;
    }
}