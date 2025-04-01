package Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatenExtraktor {

    public static String extractCity(String input) {

        String filePath = "C://Users//Amirmohammad//Desktop//Chatbot//stadtliste.txt";
        try (BufferedReader cityReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String city;
            while ((city = cityReader.readLine()) != null) {
                // Die Leere Zeilen mit if statement werden übersprungen
                // und geht nächste Zeile

                if (city.isEmpty()) {
                    continue;
                }

                if (input.trim().toLowerCase().contains(city.trim().toLowerCase())) {
                    return city;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Berlin";
    }

    public static double extractAmount(String input) {
        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group());
        }
        return -1; // Fehlerwert, falls kein Betrag gefunden wurde
    }

    public static String extractFromCurrency(String input) {
        //  String[] waehrungen = {"USD", "EUR", "GBP", "CHF", "JPY", "AUD", "CAD"};
        Pattern pattern = Pattern.compile("(\\d+\\s?)(USD|EUR|GBP|CHF|JPY|AUD|CAD)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(2).toUpperCase(); // Rückgabe der gefundenen Währung
        }

        return null; // Keine Währung gefunden
    }

    public static String extractToCurrency(String eingabe) {

        String[] waehrungen = {"USD", "EUR", "GBP", "CHF", "JPY", "AUD", "CAD"};
        for (String waehrung : waehrungen) {
            if (eingabe.toUpperCase().contains("IN " + waehrung) || eingabe.toUpperCase().contains("NACH " + waehrung)
                    || eingabe.toUpperCase().contains("ZU " + waehrung)) {
                return waehrung;
            }
        }
        return null; //kine Zielwährung gefunden
    }
}
