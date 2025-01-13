

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class ATMServer extends UnicastRemoteObject implements ATM {
    private double balance;

    protected ATMServer() throws RemoteException {
        super();
        balance = 0.0; // Initial balance
    }

    @Override
    public double checkBalance() throws RemoteException {
        return balance;
    }

    @Override
    public void deposit(double amount) throws RemoteException {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        } else {
            System.out.println("Invalid deposit amount");
        }
    }

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
