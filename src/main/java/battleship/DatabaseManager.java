package battleship;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:h2:./battleship_db";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public DatabaseManager() {
        setupDatabase();
    }

    // Cria a tabela de jogadas (se ainda não existir)
    private void setupDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS moves (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "player VARCHAR(255), " +
                "coord_x INT, " +
                "coord_y INT, " +
                "result VARCHAR(50), " +
                "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erro ao configurar a base de dados: " + e.getMessage());
        }
    }

    // Método para inserir uma jogada (tiro) na BD
    public void saveMove(String player, int x, int y, String result) {
        String sql = "INSERT INTO moves (player, coord_x, coord_y, result) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, player);
            pstmt.setInt(2, x);
            pstmt.setInt(3, y);
            pstmt.setString(4, result);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao gravar jogada: " + e.getMessage());
        }
    }

}
