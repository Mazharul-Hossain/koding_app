package RMI_Server;

import RMI_Client.RMIClientInterface;
import java.rmi.*;
import java.util.List;

public interface RMIServerInterface extends Remote {

    public abstract boolean register(String user_name, String password) throws RemoteException;

    public abstract boolean sign_in(String user_name, String password, RMIClientInterface rmiClient) throws RemoteException;

    public abstract List getUserList() throws RemoteException;

    public abstract void chatUnicast(String sender_name, String receiver_name, String msg) throws RemoteException;

    public abstract void chatBroadcast(String sender_name, String msg) throws RemoteException;
}
