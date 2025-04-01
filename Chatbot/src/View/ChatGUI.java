package View;

import Controller.ChatController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatGUI extends JFrame {

    private JPanel chatPanel; // Panel für den Chat-Bereich
    private JScrollPane scrollPane; // Scrollpane für den Chat-Bereich
    private JTextField inputField; // Eingabefeld für den Benutzer
    private JButton sendButton; // Button zum Senden der Nachricht
    private JPanel container; // Container-Panel, das den scrollbaren Bereich hält
    private JButton chatHistoryButton;

    private final String placeholderText = "Nachricht senden...";

    private ChatController controller;

    public ChatGUI() {


        setTitle("CHATTIE");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("C://Users//Amirmohammad//Desktop//Chatbot//Chatbot//src//ChatbotLogo.png").getImage());


        // Schriftart für gesamte GUI setzen
        Font standardFont = new Font("Arial", Font.PLAIN, 16);
        UIManager.put("Label.font", standardFont);
        UIManager.put("TextField.font", standardFont);
        UIManager.put("TextArea.font", standardFont);

        // Chatbereich mit Scrollpane
        container = new JPanel(new BorderLayout());

        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(new Color(245, 245, 245));

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        container.add(scrollPane, BorderLayout.CENTER);
        add(container, BorderLayout.CENTER);

        // Eingabebereich
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(230, 230, 230));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 10, 5);

        inputField = new JTextField(placeholderText);
        inputField.setForeground(Color.BLACK);

        inputField.setBackground(new Color(230, 230, 230));
        inputField.setPreferredSize(new Dimension(150, 60));

        sendButton = new JButton("Senden");
        sendButton.setBackground(new Color(255, 255, 255));
        sendButton.setForeground(Color.BLACK);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        chatHistoryButton = new JButton("Chat History");
        chatHistoryButton.setBackground(new Color(255, 255, 255));
        chatHistoryButton.setForeground(Color.BLACK);
        chatHistoryButton.setFocusPainted(false);
        chatHistoryButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));


        inputPanel.add(inputField, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.insets = new Insets(5, 5, 10, 10);
        inputPanel.add(sendButton, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.insets = new Insets(5, 5, 10, 10);
        inputPanel.add(chatHistoryButton, gbc);

        add(inputPanel, BorderLayout.SOUTH);

        // Fügt einen Fokus-Listener zum Eingabefeld hinzu
        inputField.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                // Wenn der aktuelle Text der Platzhaltertext ist, wird das Feld geleert
                if (inputField.getText().equals(placeholderText)) {
                    inputField.setText(""); // Entfernt den Platzhaltertext
                    inputField.setForeground(Color.BLACK); // Setzt die Textfarbe auf Schwarz für Benutzereingabe
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // Wenn das Eingabefeld leer ist, wird der Platzhalter wieder eingefügt
                if (inputField.getText().trim().isEmpty()) {
                    inputField.setText(placeholderText); // Setzt den Platzhaltertext zurück
                    inputField.setForeground(Color.BLACK); // (Optional) Ändert die Farbe, könnte z. B. Grau sein
                }
            }
        });

        chatHistoryButton.addActionListener(e -> controller.loadChatHistory());

        // Senden-Button mit dem Controller verbinden
        sendButton.addActionListener(e -> {
            String benutzerEingabe = inputField.getText().trim(); // Eingabetext holen und Leerzeichen entfernen
            if (!benutzerEingabe.isEmpty()) { // Prüfen, ob der Benutzer etwas eingegeben hat
                controller.processUserInput(benutzerEingabe); // Eingabe an den Controller senden
                inputField.setText(""); // Eingabefeld leeren
                inputField.setForeground(Color.BLACK); // Textfarbe zurücksetzen
            }
        });

        // Eingabefeld mit der Enter-Taste verbinden
        inputField.addActionListener(e -> {
            String benutzerEingabe = inputField.getText().trim(); // Eingabetext holen und Leerzeichen entfernen
            if (!benutzerEingabe.isEmpty()) { // Prüfen, ob der Benutzer etwas eingegeben hat
                controller.processUserInput(benutzerEingabe); // Eingabe an den Controller senden
                inputField.setText(""); // Eingabefeld leeren
                inputField.setForeground(Color.BLACK); // Textfarbe zurücksetzen
            }
        });
        setVisible(true);
    }

    // Zeigt eine Nachricht im Chat an
    public void showMessage(String message, boolean isUser) {

        JPanel messageBubble = new JPanel(new BorderLayout());
        messageBubble.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        messageBubble.setBackground(new Color(245, 245, 245, 255));

        JTextArea messageArea = new JTextArea(message);
        messageArea.setPreferredSize(null);
        messageArea.setWrapStyleWord(true);
        messageArea.setOpaque(true);
        messageArea.setEditable(false);
        messageArea.setForeground(Color.WHITE);
        messageArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        boolean isNews = message.contains("https") || message.length() > 250;
        messageArea.setLineWrap(isNews);

        if (isUser) {
            messageArea.setBackground(new Color(46, 204, 233));
            messageBubble.add(messageArea, BorderLayout.EAST);

        } else {
            messageArea.setBackground(new Color(52, 152, 219));
            messageBubble.add(messageArea, BorderLayout.WEST);
        }

        chatPanel.add(messageBubble);
        chatPanel.revalidate();
        chatPanel.repaint();
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()));
    }

    public void setChatController(ChatController chatController) {
        this.controller = chatController;
    }
}