

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client extends JFrame {
    private ATM atm; // Référence à l'objet distant
    private JTextField amountField; // Champ pour saisir le montant
    private JTextArea resultArea; // Zone pour afficher les résultats

    public Client() {
        // Configuration de la fenêtre
        setTitle("ATM Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel pour les boutons et le champ de saisie
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        // Champ de saisie pour le montant
        amountField = new JTextField(10);
        inputPanel.add(new JLabel("Montant:"));
        inputPanel.add(amountField);

        // Bouton pour vérifier le solde
        JButton checkBalanceButton = new JButton("Vérifier le solde");
        checkBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double balance = atm.checkBalance();
                    resultArea.setText("Solde actuel: " + balance);
                } catch (Exception ex) {
                    resultArea.setText("Erreur: " + ex.getMessage());
                }
            }
        });
        inputPanel.add(checkBalanceButton);

        // Bouton pour déposer de l'argent
        JButton depositButton = new JButton("Déposer");
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    atm.deposit(amount);
                    resultArea.setText("Dépôt effectué. Nouveau solde: " + atm.checkBalance());
                } catch (Exception ex) {
                    resultArea.setText("Erreur: " + ex.getMessage());
                }
            }
        });
        inputPanel.add(depositButton);

        // Bouton pour retirer de l'argent
        JButton withdrawButton = new JButton("Retirer");
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    atm.withdraw(amount);
                    resultArea.setText("Retrait effectué. Nouveau solde: " + atm.checkBalance());
                } catch (Exception eX) {
                    resultArea.setText("Erreur: " + eX.getMessage());
                }
            }
        });
        inputPanel.add(withdrawButton);

        // Zone pour afficher les résultats
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Ajouter les composants à la fenêtre
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Connexion au serveur RMI
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1090);
            atm = (ATM) registry.lookup("ATMService");
            resultArea.setText("Connecté au serveur ATM.");
        } catch (Exception e) {
            resultArea.setText("Erreur de connexion: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Lancer l'interface graphique
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client().setVisible(true);
            }
        });
    }
}