package server.src.service;

import server.src.service.serviceinterface.ServiceInterface;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.TypeInfo;

import server.src.model.Channel;
import server.src.model.Load;
import server.src.model.Type;
import server.src.model.User;
import server.src.repository.UserRepository;
import server.src.model.Status;
import server.src.model.Range;

import server.src.App;

//import App

public class UserService implements ServiceInterface {

  // AJOUTE un user dans un channel
  // Prend l'id du user à connecter et l'id du channel
  // Retourne les infos du user qui s'est connecté
  // Cible = TOUT LE MONDE
  public void userConnectChannel(Load res, Load req, AsynchronousSocketChannel client) {

    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    String channelName = req.getData().get("params").get("channelName");
    String pseudo = req.getData().get("params").get("pseudo");
    User user = UserRepository.createAndConnectUser(pseudo, channelName);
    if (user == null) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    } else {
      res.setStatus(Status.OK);
      result.put("userName", user.getPseudo());
      res.setRange(Range.EVERYONE);
    }
    data.put("result", result);
    res.setData(data);
  }

  public void userDisconnect(Load res, Load req, AsynchronousSocketChannel client) {
    // USER_DISCONNECT, // ENLEVE un user d'un channel, TRIGGER le message
    // CHANNEL_DELETE si plus aucun user
    // Prend l'id du user à déconnecter et l'id du channel
    // Retourne les infos du user qui s'est déconnecté
    // Cible = TOUT LE MONDE
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    String channel = req.getData().get("params").get("channelName");
    String pseudo = req.getData().get("params").get("pseudo");
    boolean deleted = UserRepository.disconnectUser(channel, pseudo);
    if (!deleted) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    } else {
      res.setStatus(Status.OK);
      result.put("disconnectedUser", pseudo);
      res.setRange(Range.EVERYONE);
    }
    data.put("result", result);
    res.setData(data);
  }

  public void userAnswer(Load res, Load req, AsynchronousSocketChannel client) {
    // USER_ANSWER, // ENVOIE le message du user, TRIGGER le message SCORE_REFRESH
    // Prend en paramètre le message du user, la réponse à la question et l'id du
    // user qui l'a envoyé
    // Retourne le message et les infos de l'envoyeur
    // Cible = PARTICIPANTS
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    String questionResponse = req.getData().get("params").get("questionResponse"); // how, may be smth else
    String userAnswer = req.getData().get("params").get("userAnswer");
    String pseudo = req.getData().get("params").get("pseudo");

    if (userAnswer == null) {
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    } else {
      res.setStatus(Status.OK);
      result.put("psuedo", pseudo);
      result.put("userAnswer", userAnswer);
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

  public void exit(Load res, Load req, AsynchronousSocketChannel client) throws IOException {
    App.clients.remove(client);
    System.out.println("Clients restants :");
    for (AsynchronousSocketChannel cli : App.clients) {
      System.out.println(cli.getRemoteAddress());
    }
    res.setStatus(Status.OK);
    res.setRange(Range.EVERYONE);
  }

  public void run(Load res, Load req, AsynchronousSocketChannel client){
    switch (req.getType()) {
      case USER_CONNECT:
        System.out.println("Un client veut se connecter!"); 
        userConnectChannel(r
        break;
      case USER_ANSWER:
        System.out.println("Un client veut se connecter!");
        userAnswer(res, req, client);
      case USER_DISCONNECT:
        System.out.println("Un client veut se deconnecter!");
        userDisconnect(res, req, client);
      case EXIT:
        System.out.println("Un client veut exit!");
        exit(res, req, client);
      default:
        break;
    }
  }

}
