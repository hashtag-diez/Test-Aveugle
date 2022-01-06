package server.src.service.serviceinterface;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import server.src.model.Load;

public interface ServiceInterface {
  public void run(Load res, Load req, AsynchronousSocketChannel client)
      throws IOException, SQLException, NoSuchAlgorithmException;
}
