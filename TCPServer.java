// Zaeem Qureshi 7320339
// Mike Parera 8134351

import java.net.*;
import java.io.*;
import java.util.*;

public class TCPServer {
 public static void main (String args[]) {
  int serverPort = 7896; // the server port
    List <Connection> all_connections = new ArrayList<Connection>();
     try {
         InetAddress addr = InetAddress.getLocalHost();
     
         // Get IP Address
         byte[] ipAddr = addr.getAddress();
     
         // Get hostname
         String hostname = addr.getHostName();
         System.out.println("Server Name: " + hostname + "\nServer Port: " + serverPort);
     } catch (UnknownHostException e) {
     }

  try{
   ServerSocket listenSocket = new ServerSocket(serverPort);
   System.out.println("Server is Ready");
   while(true) {
        System.out.println("listening to client sockets");
    Socket clientSocket = listenSocket.accept();
        System.out.println("connection found, creating a new connection thread");
    Connection c = new Connection(clientSocket);
        all_connections.add(c);
        c.all_connections = all_connections;
   }
  } catch(IOException e) {while (true) System.out.println("IOException Listen socket:"+e.getMessage());}
 }
}
class Connection extends Thread {
 ObjectInputStream in;
 ObjectOutputStream out;
 Socket clientSocket;
  String name;
  List <Connection> all_connections;
 public Connection (Socket aClientSocket) {
    System.out.println("creating a new connection for client" );
  try {
   clientSocket = aClientSocket;
   out = new ObjectOutputStream(clientSocket.getOutputStream());
   in = new ObjectInputStream(clientSocket.getInputStream());
   this.start();
  } catch(IOException e) {System.out.println("Connection:"+e.getMessage());}
 }
 public void run(){
    System.out.println("server thread started");
  try {                    // an echo server

    while (true) {
      try {
   // String data = in.readUTF();                   // read a line of data from the stream
   // out.writeUTF(data);
      formatted_msg msg = (formatted_msg) in.readObject();
      System.out.println("Reply: " + msg);
      if (msg.msg_ctrl == formatted_msg.CTRL.SETUP) {
        System.out.println("SETUP message: " + msg.dest);
        for (int i=0; i< all_connections.size(); i++) {
          if (this == (Connection) all_connections.get(i)) {
            if (!all_connections.contains(msg.dest))
              all_connections.get(i).name = msg.dest;
            else
              all_connections.remove(i);
          }
        }
      }
      else if (msg.msg_ctrl == formatted_msg.CTRL.NORMAL) {
        int flag = 0;
        System.out.println("NORMAL to " + msg.dest);
        System.out.println("msg destination: " + msg.dest);
        for (int i=0; i< all_connections.size(); i++) {
          if (all_connections.get(i).name.equals(msg.dest)) {
            all_connections.get(i).out.writeObject(msg.msg);
            msg.msg = "Sent to " + msg.dest;
            flag = 1;
          }
        }
        if (flag == 0) {
          msg.msg = "Error sending message";
        }
      }
      if (msg.msg_ctrl == formatted_msg.CTRL.LOOPBACK) {
        msg.msg = "Loopback";
        System.out.println("LOOPBACK from " + msg.dest);
      }
      else if (msg.msg_ctrl == formatted_msg.CTRL.TERMINATE) {
        msg.msg = "Terminating";
        clientSocket.close();
      for (int i=0; i< all_connections.size(); i++) {
        if (this == (Connection) all_connections.get(i)) {
          System.out.println("Removing connection from the list, for " + name);
          System.out.println("num connection upon removing " + all_connections.size());
          all_connections.remove(i);
          break;
        }
      }
      }
      else if (msg.msg_ctrl == formatted_msg.CTRL.BROADCAST) {
        for (int i=0; i< all_connections.size(); i++)
          all_connections.get(i).out.writeObject(msg);
      } 
      else if (msg.msg_ctrl == formatted_msg.CTRL.GET_ALL_CLIENTS)  {
        //System.out.println(all_connections.length());
        msg.msg = "";
        for (int i=0; i< all_connections.size(); i++) {
          msg.msg += all_connections.get(i).name;
          if (i != all_connections.size() - 1)
            msg.msg += ", ";
        }
      }
      out.writeObject(msg);
      System.out.println("num connection " + all_connections.size());
      Thread.sleep (5);
      }catch (EOFException e){;}
    }
  }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
  } catch(IOException e) {System.out.println("readline:"+e.getMessage());
  } catch(ClassNotFoundException e) {System.out.println("readline:"+e.getMessage());
  }catch (InterruptedException e){System.out.println("readline:"+e.getMessage());
  } finally{ try {
      clientSocket.close();
      for (int i=0; i< all_connections.size(); i++) {
        if (this == (Connection) all_connections.get(i)) {
          System.out.println("Removing connection from the list, for " + name);
          System.out.println("num connection upon removing " + all_connections.size());
          all_connections.remove(i);
          break;
        }
      }
    }catch (IOException e){/*close failed*/}}
  

 }
}
