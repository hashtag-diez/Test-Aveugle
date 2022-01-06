package server.src.service;

import server.src.service.serviceinterface.ServiceInterface;

import java.nio.channels.AsynchronousSocketChannel;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.TypeInfo;

import server.src.model.User;
import server.src.model.Load;
import server.src.model.Type;
//import App

public class UserService implements ServiceInterface {
  // private UserRepository UserRepository

  public UserService(Connection con) throws Exception {
    try {
      // this.UserRepository = new UserRepository(con);
    } catch (SQLException e) {
      throw e;
    }
  }

  public void userConnect(Load response, Load req, AsynchronousSocketChannel client)
      throws NoSuchAlgorithmException, SQLException {

    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();
    /*
     * result.put("status", status);
     * result.put("type", type);
     * data.put("result", result);
     * 
     * response.setData(data)
     */
  }

  public void run(Load res, Load req, AsynchronousSocketChannel client)
      throws IOException, NoSuchAlgorithmException, SQLException {
    switch (req.getType()) {
      case USER_CONNECT:
        System.out.println("Un client veut se connecter!");
        enterPseudo(res, req, client);
        break;
      default:
        break;
    }
  }

}