// Zaeem Qureshi 7320339
// Mike Parera 8134351
import java.net.*;
import java.io.*;
public class TCPClient {
 public static void main (String args[]) {
  // arguments supply message and hostname
  // Check command line
  if (args.length < 2) {
   System.err.println("Usage : ");
   System.err.println("java TCPClient <Message> <server>");
   System.exit (1);
  }    
  Socket s = null;
  try{
   int serverPort = 7896;
      System.out.println("starting a new client socket");
   s = new Socket(args[1], serverPort);    
   ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
   ObjectInputStream in = new ObjectInputStream( s.getInputStream());
      System.out.println("subscribing as: " + args[0]);
      formatted_msg msg = new formatted_msg(args[0], "dummy");
      formatted_msg.CTRL ctrl = formatted_msg.CTRL.SETUP;
      msg.set_ctrl(ctrl);
   // out.writeUTF(args[0]);       // UTF is a string encoding see Sn. 4.4
      Listen listen = new Listen(out, in, msg); // new thread
      User user = new User(out, in, msg);
      
  }catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
  }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
  }catch (IOException e){System.out.println("readline1:"+e.getMessage());
  }finally {if(s!=null) try {s.close();}catch (IOException e){System.out.println("close:"+e.getMessage());}}
  }
}

class Listen extends Thread {
  ObjectOutputStream out;
  ObjectInputStream in;
  formatted_msg msg;
  
  public Listen(ObjectOutputStream a, ObjectInputStream b, formatted_msg c) {
    out = a; // initializing variables
    in = b;
    msg = c;
    
    run(); 
    msg.init(msg);
    try {
    out.writeObject(msg);
    }catch (IOException e){System.out.println("readline1:"+e.getMessage());}
    running();
  }
  
  public void run() { // pasted code from the example
    try {
      System.out.println("sending " + msg);
      out.writeObject(msg);  
      System.out.println("receiving response");
   // String data = in.readUTF();     // read a line of data from the stream
      msg = (formatted_msg) in.readObject();
      System.out.println("Received: "+ msg) ; 
      Thread.sleep (1);
    }catch (ClassNotFoundException e){System.out.println("readline2:"+e.getMessage());
    }catch (InterruptedException e){System.out.println("readline3:"+e.getMessage());
    }catch (IOException e){System.out.println("readline1:"+e.getMessage());
    }
  }
  
  public void running() { // pasted code from the example
    try {
      while (true) {
        try {
   // String data = in.readUTF();     // read a line of data from the stream
      msg = (formatted_msg) in.readObject();
      System.out.println("Received: "+ msg) ; 
      Thread.sleep(1);
    }catch (IOException e){System.out.println("readline1:"+e.getMessage());
      }catch (InterruptedException e){System.out.println("readline3:"+e.getMessage());}
      }
    }catch (ClassNotFoundException e){System.out.println("readline2:"+e.getMessage());
    }
  }
}