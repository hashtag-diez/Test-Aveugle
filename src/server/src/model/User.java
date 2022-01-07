package server.src.model;

import java.nio.channels.AsynchronousSocketChannel;

public class User {
  private String pseudo;
  private int score;
  private AsynchronousSocketChannel clientSocket;

  public User(String pseudo){
    this.pseudo = pseudo;
    this.score = 0;
  }

  public AsynchronousSocketChannel getClientSocket() {
    return clientSocket;
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
