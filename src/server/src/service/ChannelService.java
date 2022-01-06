package server.src.service;

import java.nio.channels.AsynchronousSocketChannel;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.TypeInfo;

import server.src.model.Channel;
import server.src.model.Load;
import server.src.model.Type;
//import server.src.repository.ChannelRepository;
import server.src.service.serviceinterface.ServiceInterface;

public class ChannelService implements ServiceInterface {
  // private ChannelRepository ChannelRepository;

  public ChannelRepository(Connection con) throws Exception {
      try {
        // this.ChannelRepository = new ChannelRepository(con);
      } catch (SQLException e) {
        throw e;
      }
    }

  public void createChannel(Load res, Load req) throws SQLException {
    System.out.println("Le client veut créer un channel !");

    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    String name = req.getData().get("params").get("name");
    String adminUuid = Integer.parseInt(getData().get("params").get("admin"));
    if (name.equals("")) {
      result.put("status", "error"); // TODO: discuss this
      result.put("errorMessage", "Il manque des informations, veuillez réessayer"); // discuss this, should we have it
    } else {
      result.put("status", "ok");
      Channel channel = ChannelRepository.insert(name, adminUuid);
      data.put("channel", channel.getName()); // may be implement a method within load instead of doing put each time
      result.put("sendToAll", "true");
    }
    data.put("result", result);
    res.setData(data);
  }

  public void run(Load res, Load req, AsynchronousSocketChannel client)
      throws SQLException {
    switch (req.getType()) {
      case CHANNEL_CREATE:
        System.out.println("Un client veut creer un channel!");
        createChannel(res, req);
        break;
      default:
        break;
    }
  }
}
