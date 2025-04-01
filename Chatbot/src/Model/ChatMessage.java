package Model;

import java.time.LocalDateTime;

public class ChatMessage {
    private String nachricht;
    private String benutzer;
    private LocalDateTime zeitstempel;


    public ChatMessage(String nachricht, String benutzer, LocalDateTime zeitstempel) {
        this.nachricht = nachricht;
        this.benutzer = benutzer;
        this.zeitstempel = zeitstempel;
    }

    public String getNachricht() {
        return nachricht;
    }

    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }

    public String getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(String benutzer) {
        this.benutzer = benutzer;
    }

    public LocalDateTime getZeitstempel() {
        return zeitstempel;
    }

    public void setZeitstempel(LocalDateTime zeitstempel) {
        this.zeitstempel = zeitstempel;
    }

}
