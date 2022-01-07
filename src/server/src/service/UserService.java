package src.service;

import src.service.serviceinterface.ServiceInterface;

import java.nio.channels.AsynchronousSocketChannel;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.TypeInfo;

import src.model.Channel;
import src.model.Load;
import src.model.Type;
import src.model.Status;
import src.model.Range;

//import App

public class UserService implements ServiceInterface {
  // private UserRepository UserRepository

//user connection connect to channel and enter pseudo dedans!!! idée changé
// AJOUTE un user dans un channel
  // Prend l'id du user à connecter et l'id du channel
  // Retourne les infos du user qui s'est connecté 
  // Cible = TOUT LE MONDE
  public void userConnectChannel(Load response, Load req, AsynchronousSocketChannel client) {

    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();
    
    String channelId = req.getData().get("params").get("channelId");
    String pseudo = req.getData().get("params").get("pseudo");
    // Etienne-> createAndConnectUser()
    User user = UserRepository.createAndConnectUser(channelId, pseudo);
    if(!user){
      response.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    }else{
      response.setStatus(Status.OK);
      result.put("userName", user.getName());
      response.setRange(Range.EVERYONE);
    }
    data.put("result", result);
    response.setData(data);
  }
/*
  //TODO USER_DISCONNECT HOW? if user created only during game?? discuss
  USER_DISCONNECT, // ENLEVE un user d'un channel, TRIGGER le message CHANNEL_DELETE si plus aucun user
  // Prend l'id du user à déconnecter et l'id du channel
  // Retourne les infos du user qui s'est déconnecté 
  // Cible = TOUT LE MONDE

*/

  // why message ?? ?? discuss a quoi ca sert, should i verify wether answer is correct
  public void userAnswer(Load response, Load req, AsynchronousSocketChannel client) {

    /*
    USER_ANSWER, // ENVOIE le message du user, TRIGGER le message SCORE_REFRESH
    // Prend en paramètre le message du user, la réponse à la question et l'id du user qui l'a envoyé
    // Retourne le message et les infos de l'envoyeur 
    // Cible = PARTICIPANTS
    */

    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();
    
    String questionAnswer = req.getData().get("params").get("questionAnswer"); // how, may be smth else
    String userAnswer = req.getData().get("params").get("userAnswer");
    int user = Integer.parseInt(req.getData().get("params").get("userId"));
    int channelId = Integer.parseInt(req.getData().get("params").get("channelId"));

    if(userAnswer==null){
      response.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    }else{
      response.setStatus(Status.OK);
      // don't know what to do ?? 
      result.put("userName", user.getName());
      // retourner quelle message
      response.setRange(Range.EVERYONE);
    }
    data.put("result", result);
    response.setData(data);
  }

  public void run(Load res, Load req, AsynchronousSocketChannel client) {
    switch (req.getType()) {
      case USER_CONNECT:
        System.out.println("Un client veut se connecter!");
        userConnectChannel(res, req, client);
        break;
      case USER_ANSWER:
        System.out.println("Un client veut se connecter!");
        userAnswer(res, req, client);
      default:
        break;
    }
  }

}