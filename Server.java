
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            ATMServer atmServer = new ATMServer();

            // Bind the remote object to the RMI registry
            Registry registry = LocateRegistry.createRegistry(1090);
            registry.rebind("ATMService", atmServer);

            System.out.println("ATM Server is ready.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}