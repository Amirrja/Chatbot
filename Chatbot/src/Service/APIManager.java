package Service;

import java.net.http.*;
import java.net.URI;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONObject;  // Diese Bibliothek braucht man für JSON Parsing

public class APIManager {
    private static final String WETTER_API_KEY = "5c7e1d1e8be64dd8bd4153725252502";
    private static final String NEWS_API_KEY = "52678fee35c1dd9298a1794371af7863";
    private static final String WAEHRUNG_API_KEY = "d1fb61296452d3981abe6ddd";


    public static String getWetter(String city) {
        String url = "https://api.weatherapi.com/v1/current.json?key=5c7e1d1e8be64dd8bd4153725252502&q=" + city + "&aqi=yes" + WETTER_API_KEY + "&lang=en";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // JSON-String in ein JSONObject umwandeln
            JSONObject json = new JSONObject(response.body());
            // Die Wetterdaten befinden sich unter "current"
            JSONObject current = json.getJSONObject("current");
            // Wetterbeschreibung ist in "condition.text"
            String wetter = current.getJSONObject("condition").getString("text");
            // Temperatur in Celsius
            double temperatur = current.getDouble("temp_c");
            // Windgeschwindigkeit in km/h
            double wind = current.getDouble("wind_kph");
            return "Wetter in " + json.getJSONObject("location").getString("name") + ": "
                    + wetter + ", Temperatur: " + temperatur + "°C, Wind: " + wind + " km/h";
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Fehler beim Abrufen der Wetterdaten.";
        }
    }

    public static String getNews() {
        String url = "https://gnews.io/api/v4/top-headlines" + "?lang=de&token=" + NEWS_API_KEY;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONArray articles = jsonResponse.getJSONArray("articles");
            StringBuilder nachrichtenText = new StringBuilder();
            for (int i = 0; i < Math.min(5, articles.length()); i++) {
                JSONObject article = articles.getJSONObject(i);
                String title = article.getString("title");
                String urlNews = article.getString("url");
                nachrichtenText.append(i + 1).append(". ").append(title).append("\n").append(urlNews).append("\n\n");
            }
            return nachrichtenText.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Fehler beim Abrufen der Nachrichten.";
        }
    }

    public static String currencyConversion(String from, String to, double amount) {

        String baseUrl = "https://v6.exchangerate-api.com/v6/";
        String url = baseUrl + WAEHRUNG_API_KEY + "/pair/" + from + "/" + to;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());
            if (json.has("conversion_rate")) {
                double rate = json.getDouble("conversion_rate");
                double umgerechnet = amount * rate;
                return amount + " " + from + " = " + umgerechnet + " " + to;
            } else {
                return "Fehler: Ungültige Währung oder API-Problem." + "\nBITTE GEBEN SIE IN KOREKTE FORMAT ZUM BEISPIEL : 100 USD IN EUR ";
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Fehler beim Abrufen der Wechselkurse.";
        }

    }

    public static String getTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return "Aktuelle Zeit und Datum sind : " + now.format(formatter);
    }
}