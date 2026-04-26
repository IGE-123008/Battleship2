package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DatabaseManager.
 * Author: 122992
 * Date: 2026-04-26 22:42:41
 * Cyclomatic Complexity:
 * - constructor/setupDatabase(): 1
 * - saveMove(): 1
 */
class DatabaseManagerTest {

    private DatabaseManager db;

    @BeforeEach
    void setUp() {
        db = new DatabaseManager();
    }

    @AfterEach
    void tearDown() {
        // clean up DB file entries to avoid side effects
        try (Connection conn = DriverManager.getConnection("jdbc:h2:./battleship_db", "sa", "");
             Statement st = conn.createStatement()) {
            st.execute("DELETE FROM moves");
        } catch (SQLException e) {
            // ignore cleanup errors
        }
        db = null;
    }

    @Test
    void constructor() {
        assertNotNull(db, "Error: expected DatabaseManager instance but got null");
    }

    @Test
    void saveMove() throws SQLException {
        db.saveMove("TestPlayer", 1, 2, "Tiro (Barca)");

        try (Connection conn = DriverManager.getConnection("jdbc:h2:./battleship_db", "sa", "");
             PreparedStatement pstmt = conn.prepareStatement("SELECT player, coord_x, coord_y, result FROM moves ORDER BY id DESC LIMIT 1")) {
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next(), "Error: expected at least one row in moves table but got none");
            assertEquals("TestPlayer", rs.getString(1), "Error: expected player TestPlayer but got " + rs.getString(1));
            assertEquals(1, rs.getInt(2), "Error: expected coord_x 1 but got " + rs.getInt(2));
            assertEquals(2, rs.getInt(3), "Error: expected coord_y 2 but got " + rs.getInt(3));
            assertEquals("Tiro (Barca)", rs.getString(4), "Error: expected result Tiro (Barca) but got " + rs.getString(4));
        }
    }
}