import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GuessMyNumberGameUI extends JFrame implements ActionListener {
    private Random random = new Random();
    private int randomNumber;
    private int score = 0;
    private int round = 1;
    private int attempts = 0;
    private final int maxAttempts = 10;

    private JTextField guessField;
    private JButton guessButton;
    private JLabel messageLabel, scoreLabel, roundLabel, attemptsLabel;

    public GuessMyNumberGameUI() {
        setTitle("Guess My Number Game");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Gradient background panel
        JPanel contentPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(151, 0, 128); // Dark Magenta
                Color color2 = new Color(0, 255, 255); // Teal
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        setContentPane(contentPanel);
        contentPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;

        JLabel titleLabel = new JLabel("GUESS MY NUMBER GAME!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 68));
        titleLabel.setForeground(new Color(255, 153, 0));
        addComponent(contentPanel, titleLabel, gbc, 0);

        roundLabel = new JLabel("Round: " + round, SwingConstants.CENTER);
        roundLabel.setFont(new Font("Arial", Font.BOLD, 28));
        roundLabel.setForeground(Color.WHITE);
        addComponent(contentPanel, roundLabel, gbc, 1);

        scoreLabel = new JLabel("Score: " + score, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 28));
        scoreLabel.setForeground(Color.WHITE);
        addComponent(contentPanel, scoreLabel, gbc, 2);

        attemptsLabel = new JLabel("Attempts Left: " + (maxAttempts - attempts), SwingConstants.CENTER);
        attemptsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        attemptsLabel.setForeground(Color.WHITE);
        addComponent(contentPanel, attemptsLabel, gbc, 3);

        messageLabel = new JLabel("🎲 Guess a number between 1 and 100 🎲", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 26));
        messageLabel.setForeground(new Color(255, 204, 0));
        addComponent(contentPanel, messageLabel, gbc, 4);

        guessField = new JTextField();
        guessField.setFont(new Font("Arial", Font.BOLD, 36));
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessField.setPreferredSize(new Dimension(300, 60));
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        addComponent(contentPanel, guessField, gbc, 5);

        guessButton = new JButton("GUESS");
        guessButton.setFont(new Font("Arial", Font.BOLD, 32));
        guessButton.setBackground(new Color(175, 0, 143)); // Brighter magenta
        guessButton.setForeground(Color.WHITE);
        guessButton.setFocusPainted(false);
        guessButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        guessButton.addActionListener(this);
        addComponent(contentPanel, guessButton, gbc, 6);

        JLabel footerLabel = new JLabel("MADE BY G1.CORP", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        footerLabel.setForeground(Color.WHITE);
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTH;
        addComponent(contentPanel, footerLabel, gbc, 7);

        randomNumber = random.nextInt(100) + 1;
        setVisible(true);
    }

    private void addComponent(JPanel panel, Component comp, GridBagConstraints gbc, int gridy) {
        gbc.gridy = gridy;
        panel.add(comp, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String input = guessField.getText();
        guessField.setText("");

        try {
            int guess = Integer.parseInt(input);
            attempts++;
            attemptsLabel.setText("Attempts Left: " + (maxAttempts - attempts));

            if (guess < randomNumber) {
                showMessage("📉 Too low! Try again.", Color.ORANGE);
            } else if (guess > randomNumber) {
                showMessage("📈 Too high! Try again.", Color.ORANGE);
            } else {
                int roundScore = (maxAttempts - attempts + 1) * 10;
                score += roundScore;
                scoreLabel.setText("Score: " + score);
                showCustomPopup(true);
                return;
            }

            if (attempts >= maxAttempts && guess != randomNumber) {
                showCustomPopup(false);
            }

        } catch (NumberFormatException ex) {
            showMessage("⚠ Please enter a valid number.", Color.YELLOW);
        }
    }

    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }

    private void showCustomPopup(boolean won) {
        JFrame popup = new JFrame("🎉 Round Result 🎉");
        popup.setSize(400, 300);
        popup.setLayout(new BorderLayout());
        popup.setLocationRelativeTo(this);
        popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBackground(new Color(31, 97, 141));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel resultLabel = new JLabel(
                won ? "✅ Correct Guess!" : "❌ Out of attempts!", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultLabel.setForeground(Color.WHITE);

        JLabel answerLabel = new JLabel(
                won ? "" : "Correct Number was: " + randomNumber, SwingConstants.CENTER);
        answerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        answerLabel.setForeground(Color.YELLOW);

        int roundScore = won ? (maxAttempts - attempts + 1) * 10 : 0;
        JLabel scoreInfo = new JLabel("Round Score: " + roundScore + " | Total: " + score, SwingConstants.CENTER);
        scoreInfo.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreInfo.setForeground(Color.WHITE);

        JLabel roundSummary = new JLabel("Round " + round + (won ? ": Success" : ": Failed"), SwingConstants.CENTER);
        roundSummary.setFont(new Font("Arial", Font.BOLD, 16));
        roundSummary.setForeground(Color.CYAN);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(31, 97, 141));

        JButton nextRoundBtn = new JButton("Next Round");
        JButton exitBtn = new JButton("Exit Game");

        nextRoundBtn.setFocusPainted(false);
        exitBtn.setFocusPainted(false);

        nextRoundBtn.setBackground(new Color(40, 180, 99));
        nextRoundBtn.setForeground(Color.WHITE);
        exitBtn.setBackground(new Color(192, 57, 43));
        exitBtn.setForeground(Color.WHITE);

        nextRoundBtn.addActionListener(evt -> {
            popup.dispose();
            nextRound();
        });

        exitBtn.addActionListener(evt -> {
            JOptionPane.showMessageDialog(this, "🏁 Final Score: " + score + "\nThanks for playing!", "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        });

        buttonPanel.add(nextRoundBtn);
        buttonPanel.add(exitBtn);

        panel.add(resultLabel);
        panel.add(answerLabel);
        panel.add(scoreInfo);
        panel.add(roundSummary);
        panel.add(buttonPanel);

        popup.add(panel, BorderLayout.CENTER);
        popup.setVisible(true);
    }

    private void nextRound() {
        round++;
        randomNumber = random.nextInt(100) + 1;
        attempts = 0;
        guessField.setText("");
        roundLabel.setText("Round: " + round);
        attemptsLabel.setText("Attempts Left: " + maxAttempts);
        showMessage("🎲 Guess a number between 1 and 100 🎲", new Color(255, 204, 0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GuessMyNumberGameUI::new);
    }
}
