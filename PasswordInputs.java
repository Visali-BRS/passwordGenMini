import javax.swing.*;
import java.awt.*;

public class PasswordInputs {
    JTextField passwordField, baseWordField;
    JCheckBox upperCaseCheck, lowerCaseCheck, numberCheck, symbolCheck, customWordCheck;
    JSpinner lengthSpinner;

    public void setupInputs(JPanel panel, GridBagConstraints gbc) {
        gbc.gridwidth = 1; gbc.gridy = 2; gbc.gridx = 0;
        JLabel lengthLabel = new JLabel("Password Length:");
        panel.add(lengthLabel, gbc);

        lengthSpinner = new JSpinner(new SpinnerNumberModel(12, 4, 32, 1));
        gbc.gridx = 1;
        panel.add(lengthSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel baseLabel = new JLabel("Enter Base Word:");
        panel.add(baseLabel, gbc);

        baseWordField = new JTextField();
        gbc.gridx = 1;
        panel.add(baseWordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        customWordCheck = new JCheckBox("Use custom word in password");
        panel.add(customWordCheck, gbc);

        gbc.gridy++;
        upperCaseCheck = new JCheckBox("Include Uppercase (A-Z)");
        panel.add(upperCaseCheck, gbc);

        gbc.gridy++;
        lowerCaseCheck = new JCheckBox("Include Lowercase (a-z)");
        panel.add(lowerCaseCheck, gbc);

        gbc.gridy++;
        numberCheck = new JCheckBox("Include Numbers (0-9)");
        panel.add(numberCheck, gbc);

        gbc.gridy++;
        symbolCheck = new JCheckBox("Include Symbols (!@#$)");
        panel.add(symbolCheck, gbc);
    }
}