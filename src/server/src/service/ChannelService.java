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

  public void createChannel(Load req, Load res, AsynchronousSocketChannel client) {
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    String channelName = req.getData().get("params").get("channelName");
    String adminName = req.getData().get("params").get("adminName");
    String categorieName = req.getData().get("params").get("categorieName");
    String nbToursStr = req.getData().get("params").get("nbTours");

    if (channelName.equals("") || adminName.equals("") || categorieName.equals("") || nbToursStr.equals("")) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    } else {
      int nbTours = Integer.parseInt(req.getData().get("params").get("nbTours"));
      res.setStatus(Status.OK);
      Channel channel = ChannelRepository.addChannel(channelName, adminName, categorieName, nbTours, client);
      result.put("channelName", channel.getChannelName());
      result.put("categorieName", channel.getCategorie().getCategoryName());
      result.put("adminName", channel.getChannelAdmin().getPseudo());
      result.put("nbTours", String.valueOf(channel.getNbTours()));
      res.setRange(Range.EVERYONE);
    }
    data.put("result", result);
    res.setData(data);
  }

  /*
   * return names of channels and present users
   * [
   * 0 {
   * categorie: ...,
   * channelUsers: (String with users id or name divided by ",")
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

    for (Channel channel : channels) {
      Map<String, String> channelData = new HashMap<String, String>();
      List<User> players = channel.getChannelParticipants();
      channelData.put("categorie", channel.getCategorie().getCategoryName());
      channelData.put("admin", channel.getChannelAdmin().getPseudo());
      channelData.put("nbTours", channel.getNbTours() + "");
      System.out.println(players.size());
      String users = "";
      for(User player : players){
        users += player.getPseudo() + ",";
      }
      channelData.put("users", users);
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
    String categorieName = req.getData().get("params").get("categorieName").toLowerCase();
    Image image = CategorieRepository.getRandomImage(channelName, categorieName);
    String startTime = Instant.now().plus(3, ChronoUnit.SECONDS).toString();

    if (channelName.equals("")) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    } else {
      res.setStatus(Status.OK);
      ChannelRepository.start(channelName);
      result.put("response", image.getResponse());
      System.out.println(image.getResponse());
      result.put("image", image.getImg());
      result.put("channelName", channelName); // may be no need
      result.put("startTime", startTime);
      res.setRange(Range.EVERYONE);
    }
    data.put("results", result);
    res.setData(data);
  }

  public void deleteChannel(Load req, Load res) {
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    String channelName = req.getData().get("params").get("channelName");
    String adminName = req.getData().get("params").get("adminName");
    boolean deleted = ChannelRepository.deleteChannel(channelName, adminName);
    Channel channel = ChannelRepository.getChannelByName(channelName);

    if (!deleted || channel == null || adminName.equals("")) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    } else {
      res.setStatus(Status.OK);
      result.put("channelName", channel.getChannelName());
      result.put("categorieName", channel.getCategorie().getCategoryName());
      res.setRange(Range.EVERYONE);
    }
    data.put("result", result);
    res.setData(data);
  }

  public void run(Load req, Load res, AsynchronousSocketChannel client){
    switch (req.getType()) {
      case CHANNEL_CREATE:
        System.out.println("Un client veut creer un channel!");
        createChannel(req, res, client);
        break;
      case CHANNEL_DELETE:
        System.out.println("Un client veut supprimer un channel!");
        deleteChannel(req, res);
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
