package src.service;

import src.service.serviceinterface.ServiceInterface;

import java.nio.channels.AsynchronousSocketChannel;

import java.util.HashMap;
import java.util.Map;


import src.model.Load;
import src.model.User;
import src.model.Channel;
import src.repository.ChannelRepository;
import src.repository.UserRepository;
import src.model.Status;
import src.model.Range;

import src.App;

//import App

public class UserService implements ServiceInterface {

  // AJOUTE un user dans un channel
  // Prend l'id du user à connecter et l'id du channel
  // Retourne les infos du user qui s'est connecté
  // Cible = TOUT LE MONDE
  public void userConnectChannel(Load req, Load res, AsynchronousSocketChannel client) {

    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    System.out.println(req.getType());
    String channelName = req.getData().get("params").get("channelName");
    String pseudo = req.getData().get("params").get("pseudo");
    User user = UserRepository.createAndConnectUser(pseudo, channelName, client);
    if (user == null) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    } else {
      res.setStatus(Status.OK);
      result.put("userName", user.getPseudo());
      result.put("channelName", channelName);
      res.setRange(Range.EVERYONE);
    }
    data.put("result", result);
    res.setData(data);
  }

  public void userDisconnect(Load req, Load res, AsynchronousSocketChannel client) {
    // USER_DISCONNECT, // ENLEVE un user d'un channel, TRIGGER le message
    // CHANNEL_DELETE si plus aucun user
    // Prend l'id du user à déconnecter et l'id du channel
    // Retourne les infos du user qui s'est déconnecté
    // Cible = TOUT LE MONDE
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    String channelName = req.getData().get("params").get("NameName");
    String pseudo = req.getData().get("params").get("pseudo");
    boolean deleted = UserRepository.disconnectUser(pseudo, channelName);
    boolean isUserAdmin =  UserRepository.isAdmin(pseudo, channelName);

    if (!deleted) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    } else {
      Channel channel = ChannelRepository.getChannelByName(channelName);
      res.setStatus(Status.OK);
      result.put("disconnectedUser", pseudo);
      result.put("channelName", channel.getChannelName());
      result.put("categorieName", channel.getCategorieName());
      result.put("isAdmin", String.valueOf(isUserAdmin));
      res.setRange(Range.EVERYONE);
    }
    data.put("result", result);
    res.setData(data);
  }

  public void userAnswer(Load req, Load res, AsynchronousSocketChannel client) {
    // USER_ANSWER, // ENVOIE le message du user, TRIGGER le message SCORE_REFRESH
    // Prend en paramètre le message du user, la réponse à la question et l'id du
    // user qui l'a envoyé
    // Retourne le message et les infos de l'envoyeur
    // Cible = PARTICIPANTS
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    String channelName = req.getData().get("params").get("channelName");
    String questionResponse = req.getData().get("params").get("questionResponse"); // how, may be smth else
    String userAnswer = req.getData().get("params").get("userAnswer");
    String pseudo = req.getData().get("params").get("pseudo");

    if (userAnswer.equals("") || questionResponse.equals("") || channelName.equals("") || pseudo.equals("")) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    } else {
      res.setStatus(Status.OK);
      result.put("pseudo", pseudo);
      result.put("userAnswer", userAnswer);
      result.put("channelName", channelName);
      if (userAnswer == questionResponse) {
        result.put("trueOrFalse", "true");
      } else {
        result.put("trueOrFalse", "false");
      }
      res.setRange(Range.ONLY_PLAYERS);
    }
    data.put("result", result);
    res.setData(data);
  }

  public void exit(Load req, Load res, AsynchronousSocketChannel client) {
    App.clients.remove(client);
    System.out.println("Un client est parti");
    res.setStatus(Status.OK);
    res.setRange(Range.EVERYONE);
  }
  public void run(Load req, Load res, AsynchronousSocketChannel client){
    switch (req.getType()) {
      case USER_CONNECT:
        System.out.println("Un client veut se connecter!"); 
        userConnectChannel(req, res,client);
        break;
      case USER_ANSWER:
        System.out.println("Un client veut se connecter!");
        userAnswer(req, res, client);
        break;
      case USER_DISCONNECT:
        System.out.println("Un client veut se déconnecter!");
        userDisconnect(req, res, client);
        break;
      case EXIT:
        System.out.println("Un client veut exit!");
        exit(req, res, client);
      default:
        break;
    }
  }

}
