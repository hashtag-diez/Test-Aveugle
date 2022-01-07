package server.src.router;

import server.src.model.Type;
import server.src.service.serviceinterface.ServiceInterface;

import server.src.service.ChannelService;
import server.src.service.OneGameService;
import server.src.service.UserService;

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

    public void run(Request req, Response res, AsynchronousSocketChannel client) {
      ServiceInterface action = services.get(req.getType());
      action.run(req, res, client);
    }
  }
}