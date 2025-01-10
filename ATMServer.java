import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class ATMServer extends UnicastRemoteObject implements ATM {
    private double balance;

    // Constructor
    protected ATMServer() throws RemoteException {
        super();
        balance = 1000.0; // Set an initial balance
    }

    // Check the current balance
    @Override
    public double checkBalance() throws RemoteException {
        return balance;
    }

    // Deposit money into the account
    @Override
    public void deposit(double amount) throws RemoteException {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        } else {
            System.out.println("Invalid deposit amount");
        }
    }

    // Withdraw money from the account
    @Override
    public void withdraw(double amount) throws RemoteException {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
        } else {
            System.out.println("Invalid withdrawal amount or insufficient balance");
        }
    }
}
