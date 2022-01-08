package src.model;

import java.nio.channels.AsynchronousSocketChannel;

public class User {
  private String pseudo;
  private int score;
  private AsynchronousSocketChannel clientSocket;
  private int uid;

  public User(String pseudo, AsynchronousSocketChannel client){
    this.pseudo = pseudo;
    this.score = 0;
    this.clientSocket = client;
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

  public int getScore(){return score;}

  public void setScore(int score){
    this.score = score;
  }

  
}
