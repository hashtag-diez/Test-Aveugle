package src.service;

import src.service.serviceinterface.ServiceInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import src.model.Image;
import src.model.Load;
import src.model.Range;
import src.model.Status;
import src.repository.CategorieRepository;
import src.repository.ChannelRepository;
import src.repository.OneGameRepository;

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
    String categorieName = req.getData().get("params").get("categorieName").toLowerCase();
    String winnerUser = req.getData().get("params").get("pseudo"); // user that found a correct answer
    Instant startTime = Instant.now().plus(6, ChronoUnit.SECONDS);
    Image image = CategorieRepository.getRandomImage(channelName, categorieName);

    if(channelName.equals("")){
      res.setStatus(Status.ERROR);
      result.put("errorMessage", "Il manque des informations, veuillez réessayer");
    }else{
      res.setStatus(Status.OK);
      res.setRange(Range.ONLY_PLAYERS);
      result.put("startTime", startTime.toString());
      result.put("channelName", channelName);
      result.put("response", image.getResponse());
      result.put("image", image.getImg());
      result.put("winnerUser", winnerUser);
      if(!winnerUser.equals("none")){
        OneGameRepository.scored(winnerUser, channelName);
      }
    }
    data.put("result", result);
    res.setData(data);
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
