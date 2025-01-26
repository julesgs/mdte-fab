package Services;

import java.sql.*;

public class DBConnector {

    public Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:8889/fab";
        String user = "root";
        String password = "root";

        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie à la base de données !");
        } catch (ClassNotFoundException e) {
            throw new Exception("Connexion BDD : Le driver n'a pas été trouvé");
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la connexion à la base de données.");
        }

        return connection;
    }

    public void getFromBDD() {
        try (Connection conn = this.getConnection()) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String query = "SELECT * from test";
                ResultSet rs = stmt.executeQuery(query);
                System.out.println(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}