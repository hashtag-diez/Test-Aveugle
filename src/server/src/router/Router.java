package src.router;

import src.model.Load;
import src.model.Type;
import src.service.serviceinterface.ServiceInterface;

import src.service.ChannelService;
import src.service.OneGameService;
import src.service.UserService;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.HashMap;
import java.util.Map;

public class Router {
  private Map<Type, ServiceInterface> services;

  public Router(){
    services = new HashMap<Type, ServiceInterface>();
    UserService user = new UserService();
    services.put(Type.USER_CONNECT, user); //DONE
    services.put(Type.USER_ANSWER, user); //DONE 
    services.put(Type.USER_DISCONNECT, user); //DONE
    services.put(Type.EXIT, user); // DONE

    ChannelService channel = new ChannelService();
    services.put(Type.GET_CHANNELS, channel); //DONE
    services.put(Type.CHANNEL_CREATE, channel); //DONE
    services.put(Type.CHANNEL_START, channel); //DONE
    services.put(Type.CHANNEL_DELETE, channel); //DONE

    OneGameService  oneGame = new OneGameService();
    services.put(Type.SCORE_REFRESH, oneGame); //DONE
  }
  public void run(Load req, Load res, AsynchronousSocketChannel client) {
    ServiceInterface action = services.get((Type) req.getType());
    action.run(req, res, client);
  }
}