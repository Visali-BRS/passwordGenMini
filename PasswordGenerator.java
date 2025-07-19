import java.util.Random;

public class PasswordGenerator {
    public String generate(int length, boolean useUpper, boolean useLower, boolean useNumbers, boolean useSymbols, String baseWord) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String nums = "0123456789";
        String syms = "!@#$%^&*()-_=+<>?";

        StringBuilder all = new StringBuilder();
        if (useUpper) all.append(upper);
        if (useLower) all.append(lower);
        if (useNumbers) all.append(nums);
        if (useSymbols) all.append(syms);

        if (all.length() == 0) return "";

        StringBuilder password = new StringBuilder();
        Random rand = new Random();

        if (!baseWord.isEmpty()) {
            for (char c : baseWord.toCharArray()) {
                if (rand.nextBoolean()) password.append(Character.toUpperCase(c));
                else password.append(Character.toLowerCase(c));
            }
        }

        while (password.length() < length) {
            password.append(all.charAt(rand.nextInt(all.length())));
        }

        return password.substring(0, length);
    }
}