import javax.swing.*;
import java.util.logging.*;

public class PasswordAppMain {
    Logger logger = Logger.getLogger(PasswordAppMain.class.getName());

    public void setupLogger() {
        try {
            FileHandler handler = new FileHandler("app.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.setLevel(Level.INFO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MyPasswordApp());
    }
}
