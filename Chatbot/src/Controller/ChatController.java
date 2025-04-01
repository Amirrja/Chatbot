package Controller;

import Model.ChatMessage;
import Service.ChatLogik;
import Service.DBService;
import View.ChatGUI;


import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;

public class ChatController {
    private ChatLogik chatLogik;
    private ChatGUI chatGUI;
    private DBService dbService;


    public ChatController(ChatGUI chatGUI, ChatLogik chatLogik, DBService dbService) {
        this.chatGUI = chatGUI;
        this.chatLogik = chatLogik;
        this.dbService = dbService;

        showStartMsg();

    }


    public void processUserInput(String userInput) {
        if (userInput.trim().isEmpty()) return;

        chatGUI.showMessage(userInput, true); // Benutzer-Nachricht anzeigen
        dbService.saveMessage(new ChatMessage(userInput, "User", LocalDateTime.now())); // User msg wird in DB gespeichert

        // Verarbeitung durch die ChatLogik
        String botAnswer = chatLogik.process_Input(userInput);
        dbService.saveMessage(new ChatMessage(botAnswer, "Bot", LocalDateTime.now())); // Bot msg wird in DB gespeichert

        // Verzögerung, um eine realistischere Antwortzeit zu simulieren
        Timer t = new Timer(400, e -> {
            chatGUI.showMessage(botAnswer, false);
            ((Timer) e.getSource()).stop();  // Stellt sicher, dass der Timer nach einer Ausführung stoppt
        });
        t.setRepeats(false);
        t.start();
    }

    public void showStartMsg() {

        String startMessage = """
                Hallo! Ich bin Chattie, dein digitaler Assistent.\s
                Ich kann dir helfen mit:
                - Wetterabfragen
                - Nachrichten anzeigen
                - Währungsumrechnungen
                - Zeitauskunft
                Frag mich einfach!""";

        chatGUI.showMessage(startMessage, false);
    }

    public void loadChatHistory() {
        List<String> history = DBService.ladeChatverlauf();
        for (String msg : history) {
            chatGUI.showMessage(msg, false);
        }
    }
}


