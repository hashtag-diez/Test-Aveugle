package src.model;

import java.nio.channels.AsynchronousSocketChannel;

public class User {
  private String pseudo;
  private AsynchronousSocketChannel clientSocket;
  private int uid;

  public User(String pseudo){
    this.pseudo = pseudo;
  }

  public AsynchronousSocketChannel getClientSocket() {
    return clientSocket;
  }

  public int getUid() {
    return uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
  }
  public String toString(){
    return uid+":"+pseudo;
  }
  public void setClientSocket(AsynchronousSocketChannel clientSocket) {
    this.clientSocket = clientSocket;
  }

  public String getPseudo(){return pseudo;}
}
