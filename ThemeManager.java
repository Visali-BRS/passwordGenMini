import javax.swing.*;
import java.awt.*;

public class PasswordUI {

    JPanel mainPanel;
    JButton themeToggleButton;
    boolean darkTheme = true;
    Color bgDark = new Color(33, 33, 55);
    Color bgLight = new Color(245, 245, 245);
    Color fgDark = Color.WHITE;
    Color fgLight = Color.BLACK;
    Color accentDark = new Color(102, 204, 255);
    Color accentLight = new Color(0, 102, 204);

    public PasswordUI() {
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        themeToggleButton = new JButton("🌙 Switch to Light Theme");
        themeToggleButton.setFocusPainted(false);
        mainPanel.add(themeToggleButton, gbc);

        applyTheme();
    }

    public void applyTheme() {
        Color bg = darkTheme ? bgDark : bgLight;
        Color fg = darkTheme ? fgDark : fgLight;
        Color accent = darkTheme ? accentDark : accentLight;

        mainPanel.setBackground(bg);
        for (Component comp : mainPanel.getComponents()) {
            comp.setBackground(bg);
            comp.setForeground(fg);
            if (comp instanceof JButton) {
                comp.setBackground(accent);
                comp.setForeground(fg);
            }
        }

        themeToggleButton.setText(darkTheme ? "🌞 Switch to Light Theme" : "🌙 Switch to Dark Theme");
    }

    public void toggleTheme() {
        darkTheme = !darkTheme;
        applyTheme();
    }
}