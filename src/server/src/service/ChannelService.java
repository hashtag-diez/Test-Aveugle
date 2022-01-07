package src.service;

import java.nio.channels.AsynchronousSocketChannel;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import src.App;
import src.model.Channel;
import src.model.Image;
import src.model.Load;
import src.model.User;
import src.repository.CategorieRepository;
import src.repository.ChannelRepository;
import src.model.Status;
import src.model.Range;

//import src.repository.ChannelRepository;
import src.service.serviceinterface.ServiceInterface;

public class ChannelService implements ServiceInterface {

  public void createChannel(Load req, Load res) {
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    String channelName = req.getData().get("params").get("channelName");
    String adminName = req.getData().get("params").get("adminName");
    String categorieName = req.getData().get("params").get("categorieName");
    if (channelName.equals("") || adminName.equals("") || categorieName.equals("")) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    } else {
      res.setStatus(Status.OK);
      Channel channel = ChannelRepository.addChannel(channelName, adminName, categorieName);
      result.put("channelName", channel.getChannelName());
      res.setRange(Range.EVERYONE);
    }
    data.put("result", result);
    res.setData(data);
  }

  /*
   * return names of channels and present users
   * [
   * 0 {
   * response: ...,
   * image: (String with users id or name divided by ",")
   * }
   * ],
   * [
   * 1 {
   * categorie: ...,
   * channelUsers: (String with users id or name divided by ",")
   * }
   * ],
   */
  public void getChannels(Load req, Load res) {

    res.setStatus(Status.OK);

    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();

    List<Channel> channels = App.rooms;

    Map<String, String> channelData;
    for (Channel channel : channels) {
      channelData = new HashMap<String, String>();
      List<User> players = channel.getChannelParticipants();
      channelData.put("categorie", channel.getCategorie().getCategoryName());
      for(User player : players){
        channelData.put("user"+player.getUid(), player.getPseudo());
      }
      data.put(channel.getChannelName(), channelData);
    }
    res.setData(data);
    res.setRange(Range.ONLY_CLIENT);
  }

  // take in parameter channelName, return hashmap with startTime and
  // channelName(may be no need)
  public void startChannel(Load req, Load res) {

    res.setStatus(Status.OK); // TODO: discuss possibility error
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    String channelName = req.getData().get("params").get("channelName");

    if (channelName.equals("")) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    } else {
      res.setStatus(Status.OK);
      ChannelRepository.start(channelName);
      String startTime = Instant.now().plus(6, ChronoUnit.SECONDS).toString();
      result.put("channelName", channelName); // may be no need
      result.put("startTime", startTime);
      res.setRange(Range.EVERYONE);
    }
    data.put("result", result);
    res.setData(data);
  }

  public void deleteChannel(Load req, Load res) {
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    String channelName = req.getData().get("params").get("channelName");
    String adminName = req.getData().get("params").get("adminName");
    boolean deleted = ChannelRepository.deleteChannel(channelName, adminName);

    if (!deleted) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    } else {
      res.setStatus(Status.OK);
      result.put("channelName", channelName); // may be no need
      res.setRange(Range.EVERYONE);
    }
    data.put("result", result);
    res.setData(data);
  }

  public void channelQuestions(Load req, Load res) {
    // Retourne les questions selon les params données, TRIGGER le message
    // CHANNEL_START
    // Prend en paramètre l'id du channel, le nom de la catégorie
    // Retourne une Map<String,String> avec des couple <Image, réponse>
    // Cible = PARTICIPANTS
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    String categorieName = req.getData().get("params").get("categorieName");
    int nbQuestions = Integer.parseInt(req.getData().get("params").get("nbQuestions"));

    List<Image> images = CategorieRepository.getXRandomImages(nbQuestions, categorieName);

    if (images.isEmpty()) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
      data.put("result", result);
    } else {
      res.setStatus(Status.OK);
      int idImage = 0;
      Map<String, String> imageData;
      for (Image image : images) {
        imageData = new HashMap<String, String>();
        imageData.put("response", image.getResponse());
        imageData.put("image", image.getImg());
        data.put(String.valueOf(idImage), imageData);
        res.setRange(Range.ONLY_PLAYERS);
        idImage++;
      }
    }
    res.setData(data);
  }

  public void run(Load req, Load res, AsynchronousSocketChannel client){
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
