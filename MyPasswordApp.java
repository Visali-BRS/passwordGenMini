import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.StringSelection;
import java.util.Random;

public class MyPasswordApp extends JFrame implements ActionListener {

    JTextField passwordField, baseWordField;
    JButton generateButton, copyButton, themeToggleButton;
    JCheckBox upperCaseCheck, lowerCaseCheck, numberCheck, symbolCheck, customWordCheck;
    JSpinner lengthSpinner;
    JLabel strengthLabel;

    boolean darkTheme = true;
    Color bgDark = new Color(33, 33, 55);
    Color bgLight = new Color(245, 245, 245);
    Color fgDark = Color.WHITE;
    Color fgLight = Color.BLACK;
    Color accentDark = new Color(102, 204, 255);
    Color accentLight = new Color(0, 102, 204);

    JPanel mainPanel;

    public MyPasswordApp() {
        setTitle("Password Generator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 650);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        themeToggleButton = new JButton("Switch to Light Theme");
        themeToggleButton.setFocusPainted(false);
        themeToggleButton.addActionListener(e -> toggleTheme());
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(themeToggleButton, gbc);

        JLabel title = new JLabel("Secure Password Generator");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        gbc.gridy = 1;
        mainPanel.add(title, gbc);

        gbc.gridwidth = 1; gbc.gridy = 2; gbc.gridx = 0;
        JLabel lengthLabel = new JLabel("Password Length:");
        mainPanel.add(lengthLabel, gbc);

        lengthSpinner = new JSpinner(new SpinnerNumberModel(12, 4, 32, 1));
        lengthSpinner.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 1;
        mainPanel.add(lengthSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel baseLabel = new JLabel("Enter Base Word:");
        mainPanel.add(baseLabel, gbc);

        baseWordField = new JTextField(15);
        baseWordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 1;
        mainPanel.add(baseWordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        customWordCheck = createCheckBox("Use custom word in password");
        mainPanel.add(customWordCheck, gbc);

        gbc.gridy = 5;
        upperCaseCheck = createCheckBox("Include Uppercase (A-Z)");
        mainPanel.add(upperCaseCheck, gbc);

        gbc.gridy = 6;
        lowerCaseCheck = createCheckBox("Include Lowercase (a-z)");
        mainPanel.add(lowerCaseCheck, gbc);

        gbc.gridy = 7;
        numberCheck = createCheckBox("Include Numbers (0-9)");
        mainPanel.add(numberCheck, gbc);

        gbc.gridy = 8;
        symbolCheck = createCheckBox("Include Symbols (!@#$)");
        mainPanel.add(symbolCheck, gbc);

        generateButton = new JButton("Generate Password");
        generateButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        generateButton.addActionListener(this);
        gbc.gridy = 9;
        mainPanel.add(generateButton, gbc);

        passwordField = new JTextField(25);
        passwordField.setFont(new Font("Courier New", Font.BOLD, 16));
        passwordField.setEditable(false);
        gbc.gridy = 10; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(passwordField, gbc);
        gbc.fill = GridBagConstraints.NONE;

        copyButton = new JButton("Copy to Clipboard");
        copyButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        copyButton.addActionListener(e -> {
            StringSelection selection = new StringSelection(passwordField.getText());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            JOptionPane.showMessageDialog(this, "Password copied to clipboard!");
        });
        gbc.gridy = 11;
        mainPanel.add(copyButton, gbc);

        strengthLabel = new JLabel(" ");
        strengthLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        gbc.gridy = 12;
        mainPanel.add(strengthLabel, gbc);

        applyTheme();
        add(mainPanel);
        setVisible(true);
    }

    private void toggleTheme() {
        darkTheme = !darkTheme;
        applyTheme();
    }

    private void applyTheme() {
        Color bg = darkTheme ? bgDark : bgLight;
        Color fg = darkTheme ? fgDark : fgLight;
        Color accent = darkTheme ? accentDark : accentLight;

        mainPanel.setBackground(bg);
        for (Component comp : mainPanel.getComponents()) {
            comp.setBackground(bg);
            comp.setForeground(fg);
            if (comp instanceof JButton) {
                comp.setForeground(fg);
                comp.setBackground(accent);
            }
            if (comp instanceof JCheckBox) {
                comp.setForeground(fg);
            }
        }

        themeToggleButton.setText(darkTheme ? "🌞 Switch to Light Theme" : "🌙 Switch to Dark Theme");
    }

    private JCheckBox createCheckBox(String text) {
        JCheckBox cb = new JCheckBox(text);
        cb.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cb.setSelected(true);
        return cb;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int length = (Integer) lengthSpinner.getValue();
        String base = baseWordField.getText().trim();
        boolean useCustom = customWordCheck.isSelected();

        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String nums = "0123456789";
        String syms = "!@#$%^&*()-_=+<>?";

        StringBuilder all = new StringBuilder();
        if (upperCaseCheck.isSelected()) all.append(upper);
        if (lowerCaseCheck.isSelected()) all.append(lower);
        if (numberCheck.isSelected()) all.append(nums);
        if (symbolCheck.isSelected()) all.append(syms);

        if (all.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one option!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Random rand = new Random();
        StringBuilder password = new StringBuilder();

        if (useCustom && !base.isEmpty()) {
            password.append(randomizeWord(base, all.toString(), rand));
            while (password.length() < length) {
                password.append(all.charAt(rand.nextInt(all.length())));
            }
        } else {
            for (int i = 0; i < length; i++) {
                password.append(all.charAt(rand.nextInt(all.length())));
            }
        }

        passwordField.setText(password.substring(0, length));
        showStrength(password.toString());
    }

    private String randomizeWord(String base, String mixChars, Random rand) {
        StringBuilder result = new StringBuilder();
        for (char c : base.toCharArray()) {
            int chance = rand.nextInt(4);
            if (chance == 0 && Character.isLetter(c)) {
                result.append(Character.toUpperCase(c));
            } else if (chance == 1) {
                result.append(Character.toLowerCase(c));
            } else if (chance == 2) {
                result.append(rand.nextInt(10));
            } else {
                result.append(mixChars.charAt(rand.nextInt(mixChars.length())));
            }
        }
        return result.toString();
    }

    private void showStrength(String password) {
        int score = 0;
        if (password.length() >= 8) score++;
        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*[a-z].*")) score++;
        if (password.matches(".*\\d.*")) score++;
        if (password.matches(".*[!@#$%^&*()].*")) score++;

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
    }

    public static void main(String[] args) {
        new MyPasswordApp();
    }
}
