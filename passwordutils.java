 
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class PasswordUtils {
    JLabel strengthLabel;

    public String showStrength(String password) {
        int score = 0;
        if (password.length() >= 8) score++;
        if (password.matches(".[A-Z].")) score++;
        if (password.matches(".[a-z].")) score++;
        if (password.matches(".\\d.")) score++;
        if (password.matches(".[!@#$%^&()].")) score++;

        String message;
        Color color;
        if (score >= 5) {
            message = "Strength: Strong";
            color = Color.GREEN;
        } else if (score == 4) {
            message = "Strength: Good";
            color = Color.YELLOW;
        } else if (score == 3) {
            message = "Strength: Weak";
            color = Color.ORANGE;
        } else {
            message = "Strength: Very Weak";
            color = Color.RED;
        }

        strengthLabel.setText(message);
        strengthLabel.setForeground(color);
        return message;
    }

    public void savePasswordToFile(String password, String strength) {
        try (FileWriter fw = new FileWriter("passwords.txt", true)) {
            fw.write(password + " (" + strength + ")\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
} 
