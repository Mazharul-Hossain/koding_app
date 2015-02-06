/*********************************************************************

* Remote class implementation for the "Hello, world!" RMI example.

**********************************************************************/

import java.rmi.*;

import java.rmi.server.*;

public class Hello extends UnicastRemoteObject implements HelloInterface {

 private String message;

 /**

 * Construct a remote object. msg is the message of the 

 * remote object, such as "Hello, world!".

 */

 public Hello (String msg) throws RemoteException {

 message = msg;
 
 }

 /**

 * Implementation of the remotely invocable method, say().

 * Returns the message of the remote object, such as "Hello, world!".

 */

 public String say() throws RemoteException {

 return message;

 }

}