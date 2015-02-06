/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI_Server;

import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Tamanna Afreen
 */
public class Server {

    int port = 6302;
    Registry rmiRegistry;

    public Server(String ip, int port) throws RemoteException, AlreadyBoundException {

        System.out.println("Server is starting in ip: " + ip + " and port: " + port);
        this.port = port;

        System.setProperty("java.rmi.server.hostname", ip);
        rmiRegistry = LocateRegistry.createRegistry(this.port);
        //rmiRegistry = LocateRegistry.getRegistry();

        rmiRegistry.rebind("RMIchat", new RMIServer());

        System.out.println("Server is listening........");
    }

    public void close() throws NoSuchObjectException {
        // deregister the registry
        if (rmiRegistry != null) {
            System.out.println("Server is stoping........");

            UnicastRemoteObject.unexportObject(rmiRegistry, true);

            System.out.println("Server is stoped");
        }
    }
}
