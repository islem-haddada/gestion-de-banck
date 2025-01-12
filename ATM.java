package com.mycompany.tps;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ATM extends Remote {
    double checkBalance() throws RemoteException;
    void deposit(double amount) throws RemoteException;
    void withdraw(double amount) throws RemoteException;
}