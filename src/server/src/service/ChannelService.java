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
import server.src.model.Status;
import server.src.model.Range;

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

  public void createChannel(Load req, Load res) throws SQLException {
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    String name = req.getData().get("params").get("name");
    String adminUuid = Integer.parseInt(getData().get("params").get("admin"));
    if (name.equals("")) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    } else {
      res.setStatus(Status.OK);
      Channel channel = ChannelRepository.add(name, adminUuid); // ETIENNE
      result.put("channelName", channel.getName());
      res.setRange(Range.EVERYONE);
    }
    data.put("result", result);
    res.setData(data);
  }

  /*
   * return names of channels and present users
   * [
   * channelId {
   * channelName: ...,
   * categorie: ...,
   * channelUsers: (String with users id or name divided by ",")
   * }
   * ],
   * [
   * channelId {
   * channelName: ...,
   * categorie: ...,
   * channelUsers: (String with users id or name divided by ",")
   * }
   * ],
   */
  public void getChannels(Load req, Load res) throws SQLException {

    res.setStatus(Status.OK);

    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();

    // ETIENNE -> getAllChannels() in repo. ANd in channel models getName() +
    // getCuid() + getUsers() +getCategorie()
    List<Channel> channels = ChannelRepository.getChannels();

    Map<String, String> channelData;
    for (Channel channel : channels) {
      channelData = new HashMap<String, String>();
      channelData.put("channelName", channel.getName());
      channelData.put("channelUsers", channel.getUsers());
      channelData.put("categorie", channel.getCategorie());

      data.put(channel.getCuid(), channelData);
    }
    res.setData(data);
    res.setRange(Range.ONLY_CLIENT);
  }

  // reste flux
  public void startChannel(Load req, Load res) throws SQLException {
    // startGame(); // TODO
    // startTimer(); //TODO
    
    //channel attribute admin 
    // startGame(); // TODO isStarted -> dans le channel (un channel c'est ne partie)
    // startTimer(envoyer time datetime  -> get function from zeid ); //TODO

    res.setStatus(Status.OK); // TODO: discuss possibility error
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    String channelName = req.getData().get("params").get("name");
    if (name.equals("")) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    } else {
      res.setStatus(Status.OK);
      // Channel channel = ChannelRepository.getChannel(channel);
      // channel.start(); ???
      // result.put("channelName", channel.getName());
      res.setRange(Range.EVERYONE);

    }
    data.put("result", result);
    res.setData(data);
  }

  public void deleteChannel(Load req, Load res) throws SQLException {
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    int channelId = Integer.parseInt(req.getParams().get("channelId"));
    int adminUuid = Integer.parseInt(req.getParams().get("admin"));
    boolean deleted = ChannelRepository.delete(channelId, adminUuid);

    if (!deleted) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    } else {
      res.setStatus(Status.OK);
      result.put("channelId", channelId);
      // Channel channel = ChannelRepository.getChannel(channel);
      // channel.start(); ???
      // result.put("channelName", channel);
      res.setRange(Range.EVERYONE);
    }
    data.put("result", result);
    res.setData(data);
  }

  public void channelQuestions(Load req, Load res) throws SQLException {
  // Retourne les questions selon les params données, TRIGGER le message CHANNEL_START
  // Prend en paramètre l'id du channel, le nom de la catégorie 
  // Retourne une Map<String,String> avec des couple <Image, réponse>
  // Cible = PARTICIPANTS
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

  
    int channelId = Integer.parseInt(req.getParams().get("channelId"));
    String categorieName = req.getData().get("params").get("categorieName");

    // ETIENNE -> make model Question (with associated image and response) and associate it with Categorie ??
    //List<Question> questions = ChannelRepository.getQuestions(categorieName) //TODO

    if (!questions) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
      data.put("result", result)
    } else {
      res.setStatus(Status.OK);
      //  TRIGGER le message CHANNEL_START  // TODO: discuss ask???
      //Etienne -> getAnswer, getImage, getId 
      Map<String, String> questionData;
      for (Channel question : questions) {
        questionData = new HashMap<String, String>();
        questionData.put("question", question.getAnswer());
        questionData.put("image", question.getImage());
        
        data.put(question.getId(), questionData);
        res.setRange(Range.ONLY_PLAYERS);
      }
    }
    res.setData(data);
  }

  public void run(Load req, Load res, AsynchronousSocketChannel client)
      throws SQLException {
    switch (req.getType()) {
      case CHANNEL_CREATE:
        System.out.println("Un client veut creer un channel!");
        createChannel(req, res);
        break;
      case GET_CHANNELS:
        System.out.println("Un client veut get channels.");
        getChannels(req, res);
        break;
      case CHANNEL_START:
        System.out.println("Un client veut commencer un channel");
        startChannel(req, res);
        break;
      default:
        break;
    }
  }
}
