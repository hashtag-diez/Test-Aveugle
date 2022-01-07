package src.service;

import java.nio.channels.AsynchronousSocketChannel;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.TypeInfo;

import src.model.Channel;
import src.model.Load;
import src.model.Type;
import src.model.Status;
import src.model.Range;

//import src.repository.ChannelRepository;
import src.service.serviceinterface.ServiceInterface;

public class ChannelService implements ServiceInterface {

  public void createChannel(Load req, Load res) {
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    String name = req.getData().get("params").get("name");
    int adminUuid = Integer.parseInt(req.getData().get("params").get("admin"));
    if (name.equals("")) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    } else {
      res.setStatus(Status.OK);
      Channel channel = ChannelRepository.add(name, adminUuid); // ETIENNE
      result.put("channelName", channel.getChannelName());
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
  public void getChannels(Load req, Load res) {

    res.setStatus(Status.OK);

    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();

    // ETIENNE -> getAllChannels() in repo. ANd in channel models getName() +
    // getCuid() + getUsers() +getCategorie()
    List<Channel> channels = ChannelRepository.getChannels();

    Map<String, String> channelData;
    for (Channel channel : channels) {
      channelData = new HashMap<String, String>();
      channelData.put("channelName", channel.getChannelName());
      channelData.put("categorie", channel.getCategorie().getCategoryName());
      List<User> users = channel.getChannelParticipants();
      for(User user : users){
        channelData.put("User"+user.getUid(), user.toString() );
      }
      data.put("Channel"+channel.getCuid(), channelData);
    }
    res.setData(data);
    res.setRange(Range.ONLY_CLIENT);
  }

  // reste flux
  public void startChannel(Load req, Load res) {
    // informUsersAboutStartOfGame(); // TODO
    // startGame(); // TODO
    // startTimer(); //TODO
    res.setStatus(Status.OK); // TODO: discuss possibility error
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    String channelName = req.getData().get("params").get("name");
    if (channelName.equals("")) {
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

  public void deleteChannel(Load req, Load res) {
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    int channelId = Integer.parseInt(req.getData().get("params").get("channelId"));
    int adminUuid = Integer.parseInt(req.getData().get("params").get("admin"));
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

  public void channelQuestions(Load req, Load res) {
  // Retourne les questions selon les params données, TRIGGER le message CHANNEL_START
  // Prend en paramètre l'id du channel, le nom de la catégorie 
  // Retourne une Map<String,String> avec des couple <Image, réponse>
  // Cible = PARTICIPANTS
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

  
    int channelId = Integer.parseInt(req.getData().get("params").get("channelId"));
    String categorieName = req.getData().get("params").get("categorieName");

    // ETIENNE -> make model Question (with associated image and response) and associate it with Categorie ??
    //List<Question> questions = ChannelRepository.getQuestions(categorieName) //TODO

    if (!questions) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
      data.put("result", result);
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
