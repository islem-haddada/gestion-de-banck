import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BankATMClient extends JFrame {
    private ATM atm; // Référence à l'objet distant
    private JTextField amountField;
    private JTextArea resultArea;

    public BankATMClient() {
        // Configuration de la fenêtre
        setTitle("Bank ATM");
        setSize(800, 600); // Taille initiale flexible
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(true); // Permet à la fenêtre d'être redimensionnable

        // Couleurs principales
        Color primaryColor = new Color(34, 45, 65);  // Bleu foncé
        Color accentColor = new Color(41, 128, 185);  // Bleu clair
        Color textColor = Color.WHITE;

        // Barre de navigation
        JPanel navBar = new JPanel();
        navBar.setBackground(primaryColor);
        navBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel title = new JLabel("Bank ATM - Interface Client");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(textColor);
        navBar.add(title);

        // Panneau principal avec GridLayout pour rendre l'interface plus fluide
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(primaryColor);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panneau de saisie et boutons avec GridLayout pour flexibilité
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(primaryColor);
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));  // 3 lignes, 2 colonnes

        JLabel amountLabel = new JLabel("Montant :");
        amountLabel.setForeground(textColor);
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        amountField = new JTextField();
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton checkBalanceButton = createStyledButton("Vérifier le solde", accentColor, textColor);
        checkBalanceButton.addActionListener(this::checkBalance);

        JButton depositButton = createStyledButton("Déposer", accentColor, textColor);
        depositButton.addActionListener(this::depositAmount);

        JButton withdrawButton = createStyledButton("Retirer", accentColor, textColor);
        withdrawButton.addActionListener(this::withdrawAmount);

        JButton clearButton = createStyledButton("Effacer", accentColor, textColor);
        clearButton.addActionListener(_ -> resultArea.setText("")); // Efface le contenu de la zone de texte

        // Ajout des composants au panneau de saisie
        inputPanel.add(amountLabel);
        inputPanel.add(amountField);
        inputPanel.add(checkBalanceButton);
        inputPanel.add(depositButton);
        inputPanel.add(withdrawButton);
        inputPanel.add(clearButton);

        // Zone de résultats
        resultArea = new JTextArea(10, 40);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createLineBorder(accentColor, 2));
        JScrollPane resultScrollPane = new JScrollPane(resultArea);

        // Ajouter les composants au panneau principal
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(resultScrollPane, BorderLayout.CENTER);

        // Ajouter les composants à la fenêtre principale
        add(navBar, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Connexion au serveur RMI
        setupConnection();
    }

    private void setupConnection() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1090);
            atm = (ATM) registry.lookup("ATMService");
            resultArea.setText("Connecté au serveur ATM.\n");
        } catch (Exception e) {
            resultArea.setText("Erreur de connexion au serveur ATM :\n" + e.getMessage());
        }
    }

    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return button;
    }

    private void checkBalance(ActionEvent e) {
        try {
            double balance = atm.checkBalance();
            resultArea.append("Solde actuel : " + balance + " DA\n");
        } catch (Exception ex) {
            resultArea.append("Erreur : " + ex.getMessage() + "\n");
        }
    }

    private void depositAmount(ActionEvent e) {
        if (!validateInput()) return;

        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            atm.deposit(amount);
            resultArea.append("Dépôt de " + amount + " DA effectué.\n");
            checkBalance(null);
        } catch (Exception ex) {
            resultArea.append("Erreur : " + ex.getMessage() + "\n");
        }
    }

    private void withdrawAmount(ActionEvent e) {
        if (!validateInput()) return;

        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            double currentBalance = atm.checkBalance();

            if (amount > currentBalance) {
                JOptionPane.showMessageDialog(this,
                    "Le montant demandé dépasse votre solde actuel (" + currentBalance + " DA).",
                    "Erreur de retrait",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            atm.withdraw(amount);
            resultArea.append("Retrait de " + amount + " DA effectué.\n");
            checkBalance(null);
        } catch (Exception ex) {
            resultArea.append("Erreur : " + ex.getMessage() + "\n");
        }
    }

    private boolean validateInput() {
        String input = amountField.getText().trim();

        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un montant.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            double amount = Double.parseDouble(input);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Le montant doit être positif.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un montant valide (nombre).", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankATMClient().setVisible(true));
    }
}
