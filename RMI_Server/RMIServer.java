package RMI_Server;

import RMI_Client.RMIClientInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {

    Map<String, RMIClientInterface> map;

    MySQLAccess dao;

    public RMIServer() throws RemoteException {

        map = new HashMap<>();

        dao = new MySQLAccess();
    }

    @Override
    public boolean register(String user_name, String password) {

        boolean returnFlag = false;
        try {
            dao.connectDataBase();

            String query = "SELECT COUNT(1) FROM `users` WHERE `user_name` = " + user_name;
            ResultSet rs = dao.executeQuery(query);

            if (!rs.next()) {
                query = "INSERT INTO `users` (`user_name`, `password`) VALUES(" + user_name + ", " + password + ")";
                int rsUpdate = dao.executeQueryUpdate(query);

                if (rsUpdate != 0) {
                    returnFlag = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        dao.close();
        return returnFlag;
    }

    @Override
    public boolean sign_in(String user_name, String password, RMIClientInterface rmiClient) {

        boolean returnFlag = false;
        try {
            dao.connectDataBase();

            String query = "SELECT COUNT(1) FROM `users` WHERE `user_name` = " + user_name + " AND `password` = " + password;
            ResultSet rs = dao.executeQuery(query);

            if (rs.next()) {
                map.put(user_name, rmiClient);
                returnFlag = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        dao.close();

        return returnFlag;
    }

    @Override
    public List getUserList() {
        List<String> keys = new ArrayList<>();

        for (String key : map.keySet()) {
            keys.add(key);
        }
        return keys;
    }

    @Override
    public void chatUnicast(String sender_name, String receiver_name, String msg) {

        try {
            RMIClientInterface rmiClient = map.get(receiver_name);
            rmiClient.showChat(sender_name, msg);
        } catch (RemoteException ex) {
            Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void chatBroadcast(String sender_name, String msg) {

        for (Map.Entry pairs : map.entrySet()) {
            try {
                RMIClientInterface rmiClient = (RMIClientInterface) pairs.getValue();
                rmiClient.showChat(sender_name, msg);
            } catch (RemoteException ex) {
                Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    boolean checkString(String myString) {
        return (myString != null && !myString.isEmpty());
    }
    /*===============================================
     *
     *===============================================*/

    public class MySQLAccess {

        /*
         * DB information    
         */
        private final String dbServer = "localhost";
        private final String dbName = "rmi_chat";
        private final String dbUserID = "root";
        private final String dbPass = "nopass123";

        private Connection connect = null;
        private Statement statement = null;

        public void connectDataBase() {
            try {
                // this will load the MySQL driver, each DB has its own driver
                Class.forName("com.mysql.jdbc.Driver");
                // setup the connection with the DB.
                connect = DriverManager
                        .getConnection("jdbc:mysql://" + dbServer + "/" + dbName + "?"
                                + "user=" + dbUserID + "&password=" + dbPass);

                // statements allow to issue SQL queries to the database
                statement = connect.createStatement();
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(MySQLAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public ResultSet executeQuery(String query) {
            try {
                ResultSet resultSetLocal = statement.executeQuery(query);
                return resultSetLocal;
            } catch (SQLException ex) {
                Logger.getLogger(MySQLAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        public int executeQueryUpdate(String query) {
            int resultSetLocal = 0;
            try {
                resultSetLocal = statement.executeUpdate(query);
            } catch (SQLException ex) {
                Logger.getLogger(MySQLAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
            return resultSetLocal;
        }

        // you need to close all three to make sure
        private void close() {
            try {
                statement.close();
                connect.close();
            } catch (SQLException ex) {
                Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
