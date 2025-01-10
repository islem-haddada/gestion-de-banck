import java.rmi.Naming;

public class ATMClientApp {
    public static void main(String[] args) {
        try {
            // Lookup the ATM service in the RMI registry
            ATM atm = (ATM) Naming.lookup("rmi://localhost/ATMService");

            // Perform operations using the ATM service
            System.out.println("Initial Balance: " + atm.checkBalance());

            atm.deposit(500.0);
            System.out.println("Balance after deposit: " + atm.checkBalance());

            atm.withdraw(200.0);
            System.out.println("Balance after withdrawal: " + atm.checkBalance());
        } catch (Exception e) {
            System.err.println("ATM Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
