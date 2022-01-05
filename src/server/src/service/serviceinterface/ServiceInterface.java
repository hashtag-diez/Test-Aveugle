package server.src.service.serviceinterface;
import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public interface ServiceInterface {
  public void run(AsynchronousSocketChannel client) throws IOException, SQLException, NoSuchAlgorithmException;
}
