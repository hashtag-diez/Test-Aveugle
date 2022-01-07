package src.service.serviceinterface;
import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;


import src.model.Load;

public interface ServiceInterface {
  public void run(Load res, Load req, AsynchronousSocketChannel client);
}
