package server.src.service;

import java.nio.channels.AsynchronousSocketChannel;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.TypeInfo;

import server.src.App;
import server.src.model.Channel;
import server.src.model.Image;
import server.src.model.Load;
import server.src.model.Type;
import server.src.repository.CatalogueRepository;
import server.src.repository.CategorieRepository;
import server.src.repository.ChannelRepository;
import server.src.model.Status;
import server.src.model.Range;

//import server.src.repository.ChannelRepository;
import server.src.service.serviceinterface.ServiceInterface;

public class ChannelService implements ServiceInterface {

  public ChannelService(){}

  public void createChannel(Load req, Load res) throws SQLException {
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    String name = req.getData().get("params").get("name");
    String admin = req.getData().get("params").get("admin");
    String categ = req.getData().get("params").get("categorie");
    if (name.equals("")) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    } else {
      res.setStatus(Status.OK);
      Channel channel = ChannelRepository.addChannel(name, admin, categ);
      result.put("channelName", channel.getChannelName());
      res.setRange(Range.EVERYONE);
    }
    data.put("result", result);
    res.setData(data);
  }

  /*
   * return names of channels and present users
   * [
   * channelName {
   * categorie: ...,
   * channelUsers: (String with users id or name divided by ",")
   * }
   * ],
   * [
   * channelName {
   * categorie: ...,
   * channelUsers: (String with users id or name divided by ",")
   * }
   * ],
   */
  public void getChannels(Load req, Load res) throws SQLException {

    res.setStatus(Status.OK);

    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();

    List<Channel> channels = App.rooms;

    Map<String, String> channelData;
    for (Channel channel : channels) {
      channelData = new HashMap<String, String>();
      
      channelData.put("channelUsers", channel.getChannelParticipants());
      channelData.put("categorie", channel.getCategorie());

      data.put(channel.getChannelName(), channelData);
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

    String channel = req.getParams().get("channelName");
    String admin = req.getParams().get("admin");
    boolean deleted = ChannelRepository.deleteChannel(channel, admin);

    if (!deleted) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    } else {
      res.setStatus(Status.OK);
      result.put("channelName", channel);
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

  
    String channel = req.getParams().get("channelName");
    String categorieName = req.getData().get("params").get("categorieName");
    int nbQuestions = Integer.parseInt(req.getData().get("params").get("nbQuestions"));

    // ETIENNE -> make model Question (with associated image and response) and associate it with Categorie ??
    List<Image> images = CategorieRepository.getXRandomImages(nbQuestions, categorieName);


    if (!images) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
      data.put("result", result);
    } else {
      res.setStatus(Status.OK);
      //  TRIGGER le message CHANNEL_START  // TODO: discuss ask???
      int idImage = 0;
      Map<String, String> imageData;
      for (Image image : images) {
        imageData = new HashMap<String, String>();
        imageData.put("image", image.getResponse());
        imageData.put("image", image.getImg());
        data.put(idImage, imageData);
        res.setRange(Range.ONLY_PLAYERS);
        idImage++;
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
