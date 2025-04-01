package Service;

import Model.ChatMessage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBService {
    private static final String URL = "jdbc:postgresql://localhost:5432/ChatbotSQL";
    private static final String USER = "postgres";
    private static final String PASS = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public void saveMessage(ChatMessage message) {
        String query = "INSERT INTO chatmessage (benutzer, nachricht, zeitstempel) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, message.getBenutzer());
            pstmt.setString(2, message.getNachricht());
            pstmt.setTimestamp(3, Timestamp.valueOf(message.getZeitstempel()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> ladeChatverlauf() {
        List<String> verlauf = new ArrayList<>();
        String query = "SELECT benutzer , nachricht , zeitstempel FROM chatmessage ORDER BY zeitstempel ASC ";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String eintrag = rs.getString("zeitstempel") + " _ " +
                        rs.getString("benutzer") + ": " +
                        rs.getString("nachricht");
                verlauf.add(eintrag);
            }

        } catch (SQLException e) {
            System.out.println("Fehler beim Laden der Benutzer");
            e.printStackTrace();
        }
        return verlauf;
    }


}
