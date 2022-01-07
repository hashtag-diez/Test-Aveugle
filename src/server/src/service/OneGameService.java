package server.src.service;

import server.src.service.serviceinterface.ServiceInterface;

import java.util.HashMap;
import java.util.Map;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import server.src.model.User;
import server.src.model.Load;
import server.src.model.Range;
import server.src.model.Type;
import server.src.model.Status;

import server.src.repository.OneGameRepository;

import java.nio.channels.AsynchronousSocketChannel;

public class OneGameService implements ServiceInterface {
  /*
   * SCORE_REFRESH, //RESET des timers, INCREMENTE si réponse trouvée
   * Prend en paramètre le nom du channel à démarrer
   * Retourne le nouveau score et l'heure de démarrage des timers
   * PARTICIPANTS
   * case 1 et 2:
   * 1 -> qq'un a repondu
   * 2 -> reponse a ete trouve ou pas a la fin de timer
   */
  public void scoreRefresh(Load req, Load res, AsynchronousSocketChannel client) {
    Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> result = new HashMap<String, String>();

    String channelName = req.getData().get("params").get("channelName");
    String winnerUser = req.getData().get("params").get("pseudo"); // user that found a correct answer
    String startTime = Instant.now().plus(6, ChronoUnit.SECONDS).toString();

    if(channelName.equals("") || startTime.equals("")){
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    }else if(!winnerUser.equals("")){
      User user = OneGameRepository.scored(winnerUser, channelName);
      if(user == null){
        res.setStatus(Status.ERROR);
        result.put("errorMessage", "Il manque des informations, veuillez réessayer");
      }else{
        res.setStatus(Status.OK);
        res.setRange(Range.ONLY_PLAYERS);
        result.put("userNewScore", String.valueOf(user.getScore()));
        result.put("startTime", startTime)
      }
    }else{
      res.setStatus(Status.OK);
      res.setRange(Range.ONLY_PLAYERS);
      result.put("startTime", startTime);
    }
    data.put("result", result);
  }

  public void run(Load res, Load req, AsynchronousSocketChannel client) {
    switch (req.getType()) {
      case SCORE_REFRESH:
        System.out.println("Un client veut resfresh son score!");
        scoreRefresh(res, req, client);
        break;
      default:
        break;
    }
  }
}
