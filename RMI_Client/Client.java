/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI_Client;

import RMI_Server.RMIServerInterface;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tamanna Afreen
 */
public class Client {

    RMIServerInterface rmiServer;
    RMIClientInterface rmiClient;

    String my_user_name;

    ClientGUI clientGUI;

    //ip = 54.173.191.128
    //port = 6302
    public Client(String ip, int port, ClientGUI clientGUI) throws RemoteException, NotBoundException {

        Registry registry = LocateRegistry.getRegistry(ip, port);
        rmiServer = (RMIServerInterface) registry.lookup("RMIchat");

        this.clientGUI = clientGUI;

        rmiClient = new RMIClient(this.clientGUI);
    }

    public void register(String user_name, String password) {
        try {
            if (rmiServer.register(user_name, password)) {
                clientGUI.showChat("Server", "You  have successfully registered !");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sign_in(String user_name, String password) {
        try {
            if (rmiServer.sign_in(user_name, password, rmiClient)) {
                my_user_name = user_name;

                clientGUI.showChat("Server", "You  have successfully signed in !");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getUserList() {
        try {
            clientGUI.setAvailableClientList(rmiServer.getUserList());
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void chatUnicast(String receiver_name, String msg) {
        try {
            rmiServer.chatUnicast(my_user_name, receiver_name, msg);
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void chatBroadcast(String msg) {
        try {
            rmiServer.chatBroadcast(my_user_name, msg);
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
