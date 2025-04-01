package Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChatLogik {
    private final Map<String, String> FRAGEUNDANTWORTEMAP;


    public ChatLogik() {
        FRAGEUNDANTWORTEMAP = new HashMap<>();

        loadPredefinedAnswers();
    }

    //Wird die Datei hochgeladen
    public void loadPredefinedAnswers() {
        String filePath = "C://Users//Amirmohammad//Desktop//Chatbot//chatbot_FA.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Die Leere Zeilen mit if statement werden übersprungen
                // und geht nächste Zeile
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|", 2); // jede zeile wird in 2 teilen geteilt
                //String [] teile = {frage,antwort}
                if (parts.length == 2) {
                    String question = parts[0].trim();
                    String answer = parts[1].trim();
                    // Werden die Fragen und die Antworten in Hashmap hinzugefügt
                    FRAGEUNDANTWORTEMAP.put(question.toLowerCase(), answer);
                } else {
                    System.out.println("Ungültiges Format in der Zeile: " + line);
                }

            }

            System.out.println("Vordefinierete Antworten erfolgreich geladen.");

        } catch (IOException e) {
            System.out.println("Fehler beim Laden der Datei " + e.getMessage());
        }
    }

    public String findKeywords(String input) {
        // Der Eingabetext wird zu KleinBuchstaben konvertiert
        String lowerInput = input.toLowerCase();
        //Schlüsselwörter fürs Wetter
        if (lowerInput.contains("wetter") || lowerInput.contains("temperatur") || lowerInput.contains("regnet") ||
                lowerInput.contains("sonnig") || lowerInput.contains("windig") || lowerInput.contains("schneit") ||
                lowerInput.contains("wetterbericht") || lowerInput.contains("vorhersage") || lowerInput.contains("wie viel grad")) {
            return "wetter";
        }
        //Schlüsselwörter für die Uhrzeit
        if (lowerInput.contains("uhrzeit") || lowerInput.contains("wie spät") || lowerInput.contains("wie viel uhr")
                || lowerInput.contains("uhr") || lowerInput.contains("datum") || lowerInput.contains("welcher tag")) {
            return "zeit";
        }
        //Schlüsselwörter für nachricht
        if (lowerInput.contains("nachricht") || lowerInput.contains("news") || lowerInput.contains("nachrichten") || lowerInput.contains("passiert")) {
            return "news";
        }
        //Schlüsselwörter für das Währungsumrechnen
        if (lowerInput.contains("wechselkurs") || lowerInput.contains("umrechnung") || lowerInput.contains("währung") ||
                lowerInput.contains("geld wechseln") || lowerInput.contains("wie viel ist") || lowerInput.contains("eur") ||
                lowerInput.contains("usd") || lowerInput.contains("gpb") || lowerInput.contains("wechseln") || lowerInput.contains("wie viel sind") ||
                lowerInput.contains("geld")) {

            return "währung";
        }

        return "unbekannt";
    }

    public String process_Input(String input) {
        //Überprüft ob die Frage in Hashmap vorhanden ist wenn ja dann wird die passende Antwort zurück gegeben
        String lowerInput = input.toLowerCase();
        if (FRAGEUNDANTWORTEMAP.containsKey(lowerInput)) {
            return FRAGEUNDANTWORTEMAP.get(lowerInput);
        }

        String category = findKeywords(input);

        switch (category) {
            case "wetter":
                String city = DatenExtraktor.extractCity(input);
                return APIManager.getWetter(city);
            case "news":
                return APIManager.getNews();
            case "währung":
                String fromCurrency = DatenExtraktor.extractFromCurrency(input);
                String toCurrency = DatenExtraktor.extractToCurrency(input);
                double amount = DatenExtraktor.extractAmount(input);
                return APIManager.currencyConversion(fromCurrency, toCurrency, amount);
            case "zeit":
                return APIManager.getTime();
            default:
                return generateRandomAnswer();
        }
    }

    public String generateRandomAnswer() {
        final String[] standardAnswers = {"Das habe ich nicht ganz verstanden", "Könntest du das bitte genauer erklären",
                "Ich konnte dazu nichts finden. Vielleicht kannst du es anders formulieren?", "Tut mir leid, ich habe dazu gerade keine passenden Infos."
                , "Ich konnte leider nichts Passendes finden. Soll ich es anders suchen?", "Ich bin gut, aber nicht perfekt. Vielleicht kannst du mir mehr Details geben?"
                , "Selbst meine Datenbank kratzt sich gerade am Kopf. Magst du es anders formulieren?", "Ich hab gesucht und gesucht… aber anscheinend ist die Antwort im Urlaub."
                , "Ich bin wohl noch nicht schlau genug für diese Frage. Kannst du mir eine leichtere geben?", "Ich habe überall gesucht – unter den digitalen Sofakissen, in den versteckten Ordnern – nichts!"
                , "Mein innerer Sherlock Holmes versagt leider… Kannst du mir mehr Hinweise geben?", "Ich schwöre, die Antwort war hier… bis sie sich unsichtbar gemacht hat!"
        };
        Random random = new Random();
        int standardAnswerIndex = random.nextInt(standardAnswers.length);
        return standardAnswers[standardAnswerIndex];
    }

}
