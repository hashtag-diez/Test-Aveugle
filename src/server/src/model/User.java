package server.src.model;

import java.nio.channels.AsynchronousSocketChannel;

public class User {
  private AsynchronousSocketChannel clientSocket;

  public AsynchronousSocketChannel getClientSocket() {
    return clientSocket;
  }

  public void setClientSocket(AsynchronousSocketChannel clientSocket) {
    this.clientSocket = clientSocket;
  }
}
