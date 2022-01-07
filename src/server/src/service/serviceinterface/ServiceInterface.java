package server.src.service.serviceinterface;
import java.nio.channels.AsynchronousSocketChannel;


import server.src.model.Load;

public interface ServiceInterface {
  public void run(Load res, Load req, AsynchronousSocketChannel client);
}
