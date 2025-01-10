import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ATMServerApp {
    public static void main(String[] args) {
        try {
            // Create an instance of the ATMServer
            ATM atmServer = new ATMServer();

            // Start the RMI registry on port 1099
            LocateRegistry.createRegistry(1099);

            // Bind the ATM server object to a name in the registry
            Naming.rebind("ATMService", atmServer);

            System.out.println("ATM Server is ready and waiting for client requests...");
        } catch (Exception e) {
            System.err.println("ATM Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
