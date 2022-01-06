package server.src.model;

import java.nio.channels.AsynchronousSocketChannel;

public class User {
  private String pseudo;
  private AsynchronousSocketChannel clientSocket;

  public User(String pseudo){
    this.pseudo = pseudo;
  }

  public AsynchronousSocketChannel getClientSocket() {
    return clientSocket;
  }

  public void setClientSocket(AsynchronousSocketChannel clientSocket) {
    this.clientSocket = clientSocket;
  }

  public String getPseudo(){return pseudo;}
}
