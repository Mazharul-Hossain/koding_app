/***************************************************************

* Remote Interface for the "Hello, world!" example

*****************************************************************/

import java.rmi.*;

public interface HelloInterface extends Remote {

 /**

 * Remotely invocable method, say().

 */

 public String say() throws RemoteException;

}