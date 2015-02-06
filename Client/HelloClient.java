/***************************************************************

* Client program for the "Hello, world!" RMI example.

*****************************************************************/

import java.rmi.Naming;

public class HelloClient 

{

 public static void main (String[] argv) {

 try {

 HelloInterface hello = // your PC address here

 (HelloInterface) Naming.lookup ("rmi://54.173.191.128:1099/Hello");

 System.out.println (hello.say());

 }catch (Exception e){

System.out.println ("HelloClient exception: " + e); } 

 }

}