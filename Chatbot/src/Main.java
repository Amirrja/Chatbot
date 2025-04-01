import Controller.ChatController;
import Service.ChatLogik;
import Service.DBService;
import View.ChatGUI;

public class Main {
    public static void main(String[] args) {

        ChatLogik logik = new ChatLogik();
        ChatGUI gui = new ChatGUI();
        DBService dbService = new DBService();
        ChatController controller = new ChatController(gui, logik,dbService);
        gui.setChatController(controller);

    }
}
