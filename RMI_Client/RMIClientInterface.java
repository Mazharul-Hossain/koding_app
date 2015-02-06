package RMI_Client;

import java.rmi.*;

public interface RMIClientInterface extends Remote {

    public abstract void showChat(String sender_name, String msg) throws RemoteException;
}
