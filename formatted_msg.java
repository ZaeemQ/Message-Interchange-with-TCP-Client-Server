// Zaeem Qureshi 7320339
// Mike Parera 8134351

import java.io.*;
import java.util.*;
public class formatted_msg implements Serializable{
  public enum CTRL { NORMAL, TERMINATE, LOOPBACK, BROADCAST, SETUP, GET_ALL_CLIENTS};
  String dest;
  String msg;
  CTRL msg_ctrl;
  public formatted_msg (String dst, String msg){
    this.msg = msg;
    this.dest = dst;
    msg_ctrl = CTRL.NORMAL;
  }
  public void set_terminate() { msg_ctrl = CTRL.TERMINATE; }
  public void set_loopback() { msg_ctrl = CTRL.LOOPBACK; }
  public void set_ctrl(CTRL ctrl) { msg_ctrl = ctrl; }
  public String toString(){
    String str = "formatted_msg to " + dest + " msg: " + msg;
    switch (msg_ctrl) {
      case NORMAL: str += " NORMAL"; break;
      case TERMINATE: str += " TERMINATE"; break;
      case LOOPBACK: str += " LOOPBACK"; break;
      case BROADCAST: str += " BROADCAST"; break;
      case SETUP: str += " SETUP"; break;
      case GET_ALL_CLIENTS: str += " GET_ALL_CLIENTS"; break;
    }
    return str;
  }
  static formatted_msg init(formatted_msg msg){
    Scanner sc = new Scanner(System.in);
    System.out.print("Message type: ");
    String temp = new String();
    temp = sc.nextLine();
    if (temp.toUpperCase().equals("NORMAL"))
      msg.set_ctrl(CTRL.NORMAL);
    if (temp.toUpperCase().equals("TERMINATE")) {
      msg.set_ctrl(CTRL.TERMINATE);
      return msg;
    }
    if (temp.toUpperCase().equals("LOOPBACK")) {
      msg.set_ctrl(CTRL.LOOPBACK);
      return msg;
    }
    if (temp.toUpperCase().equals("BROADCAST")) {
      msg.set_ctrl(CTRL.BROADCAST);
      return msg;
    }
    if (temp.toUpperCase().equals("SETUP"))
      msg.set_ctrl(CTRL.SETUP);
    if (temp.toUpperCase().equals("GET_ALL_CLIENTS")) {
      msg.set_ctrl(CTRL.GET_ALL_CLIENTS);
      return msg;
    }
    System.out.print("Destination: ");
    msg.dest = sc.nextLine();
    System.out.print("Content: ");
    msg.msg = sc.nextLine();
    return msg;
  };
}
