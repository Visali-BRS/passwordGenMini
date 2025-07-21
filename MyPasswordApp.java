import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.StringSelection;
import java.util.*;
import java.util.logging.*;
import java.io.*;

public class MyPasswordApp extends JFrame implements ActionListener {

    JTextField passwordField, baseWordField;
    JButton generateButton, copyButton, themeToggleButton, historyButton, undoButton;
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

    ArrayList<String> passwordHistory = new ArrayList<>();
    Stack<String> undoStack = new Stack<>();
    Logger logger = Logger.getLogger(MyPasswordApp.class.getName());

    public MyPasswordApp() {
        setTitle("Password Generator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 650);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL; // allow components to stretch
        gbc.weightx = 1;

        // Theme toggle button
        themeToggleButton = new JButton("🌙 Switch to Light Theme");
        themeToggleButton.setFocusPainted(false);
        themeToggleButton.addActionListener(e -> toggleTheme());
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(themeToggleButton, gbc);

        // Title
        JLabel title = new JLabel("Password Generator", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridy = 1;
        mainPanel.add(title, gbc);

        // Password Length Label
        gbc.gridwidth = 1; gbc.gridy = 2; gbc.gridx = 0;
        JLabel lengthLabel = new JLabel("Password Length:");
        mainPanel.add(lengthLabel, gbc);

        // Spinner for length
        lengthSpinner = new JSpinner(new SpinnerNumberModel(12, 4, 32, 1));
        lengthSpinner.setPreferredSize(new Dimension(200, 30)); // make wider
        gbc.gridx = 1;
        mainPanel.add(lengthSpinner, gbc);

        // Base word field
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel baseLabel = new JLabel("Enter Base Word:");
        mainPanel.add(baseLabel, gbc);

        baseWordField = new JTextField();
        baseWordField.setPreferredSize(new Dimension(200, 30)); // make wider
        gbc.gridx = 1;
        mainPanel.add(baseWordField, gbc);

        // Checkboxes
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        customWordCheck = createCheckBox("Use custom word in password");
        mainPanel.add(customWordCheck, gbc);

        gbc.gridy++;
        upperCaseCheck = createCheckBox("Include Uppercase (A-Z)");
        mainPanel.add(upperCaseCheck, gbc);

        gbc.gridy++;
        lowerCaseCheck = createCheckBox("Include Lowercase (a-z)");
        mainPanel.add(lowerCaseCheck, gbc);

        gbc.gridy++;
        numberCheck = createCheckBox("Include Numbers (0-9)");
        mainPanel.add(numberCheck, gbc);

        gbc.gridy++;
        symbolCheck = createCheckBox("Include Symbols (!@#$)");
        mainPanel.add(symbolCheck, gbc);

        // Generate button
        generateButton = new JButton("Generate Password");
        generateButton.setPreferredSize(new Dimension(200, 35));
        generateButton.addActionListener(this);
        gbc.gridy++;
        mainPanel.add(generateButton, gbc);

        // Password field
        passwordField = new JTextField();
        passwordField.setFont(new Font("Courier New", Font.BOLD, 16));
        passwordField.setEditable(false);
        passwordField.setPreferredSize(new Dimension(300, 35));
        gbc.gridy++;
        mainPanel.add(passwordField, gbc);

        // Copy button
        copyButton = new JButton("Copy to Clipboard");
        copyButton.setPreferredSize(new Dimension(200, 35));
        copyButton.addActionListener(e -> copyToClipboard());
        gbc.gridy++;
        mainPanel.add(copyButton, gbc);

        // Undo button
        undoButton = new JButton("Undo Password");
        undoButton.setPreferredSize(new Dimension(200, 35));
        undoButton.addActionListener(e -> undoPassword());
        gbc.gridy++;
        mainPanel.add(undoButton, gbc);

        // Strength label
        strengthLabel = new JLabel(" ");
        strengthLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        gbc.gridy++;
        mainPanel.add(strengthLabel, gbc);

        // Show history button
        historyButton = new JButton("Show History");
        historyButton.setPreferredSize(new Dimension(200, 35));
        historyButton.addActionListener(e -> showHistory());
        gbc.gridy++;
        mainPanel.add(historyButton, gbc);

        applyTheme();
        add(mainPanel);
        setVisible(true);

        setupLogger();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int length = (Integer) lengthSpinner.getValue();
            String base = baseWordField.getText().trim();
            boolean useCustom = customWordCheck.isSelected();

            String generatedPassword = generatePassword(length, base, useCustom);
            passwordField.setText(generatedPassword);

            String strength = showStrength(generatedPassword);
            passwordHistory.add(generatedPassword);
            undoStack.push(generatedPassword);

            logger.info("Generated password: " + generatedPassword + " (Strength: " + strength + ")");
            savePasswordToFile(generatedPassword, strength);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error generating password", ex);
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
        }
    }

    private String generatePassword(int length, String base, boolean useCustom) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String nums = "0123456789";
        String syms = "!@#$%^&*()-_=+<>?";

        StringBuilder all = new StringBuilder();
        if (upperCaseCheck.isSelected()) all.append(upper);
        if (lowerCaseCheck.isSelected()) all.append(lower);
        if (numberCheck.isSelected()) all.append(nums);
        if (symbolCheck.isSelected()) all.append(syms);

        if (all.length() == 0) throw new IllegalArgumentException("Please select at least one option!");

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

        return password.substring(0, length);
    }

    private void copyToClipboard() {
        StringSelection selection = new StringSelection(passwordField.getText());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
        JOptionPane.showMessageDialog(this, "Password copied to clipboard!");
        logger.info("Password copied to clipboard: " + passwordField.getText());
    }

    private void undoPassword() {
        if (!undoStack.isEmpty()) {
            undoStack.pop();
            if (!undoStack.isEmpty()) {
                passwordField.setText(undoStack.peek());
            } else {
                passwordField.setText("");
            }
        }
    }

    private void showHistory() {
        StringBuilder history = new StringBuilder("Password History:\n");
        for (String p : passwordHistory) {
            history.append(p).append("\n");
        }
        JOptionPane.showMessageDialog(this, history.toString());
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

    private String showStrength(String password) {
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

    private void savePasswordToFile(String password, String strength) {
        try (FileWriter fw = new FileWriter("passwords.txt", true)) {
            fw.write(password + " (" + strength + ")\n");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Failed to save password to file", ex);
        }
    }

    private void toggleTheme() {
        darkTheme = !darkTheme;
        applyTheme();
        logger.info("Theme toggled to " + (darkTheme ? "Dark" : "Light"));
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
                comp.setBackground(accent);
                comp.setForeground(fg);
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

    private void setupLogger() {
        try {
            FileHandler handler = new FileHandler("app.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.setLevel(Level.INFO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyPasswordApp::new);
    }
}
