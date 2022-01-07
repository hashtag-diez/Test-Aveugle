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
    services.put(Type.USER_CONNECT, user);
    services.put(Type.USER_ANSWER, user);
    services.put(Type.USER_DISCONNECT, user);
    services.put(Type.EXIT, user);

    ChannelService channel = new ChannelService();
    services.put(Type.GET_CHANNELS, channel);
    services.put(Type.CHANNEL_CREATE, channel);
    services.put(Type.CHANNEL_START, channel);
    services.put(Type.CHANNEL_DELETE, channel);
    services.put(Type.CHANNEL_QUESTIONS, channel);

    OneGameService  oneGame = new OneGameService();
    services.put(Type.SCORE_REFRESH, oneGame);
  }
  public void run(Load req, Load res, AsynchronousSocketChannel client) {
    ServiceInterface action = services.get(req.getType());
    action.run(req, res, client);
  }
}