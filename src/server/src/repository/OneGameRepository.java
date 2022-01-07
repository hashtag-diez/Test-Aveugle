package src.repository;

import src.model.User;

public class OneGameRepository {

  public static User scored(String user, String channel) {
    User u = ChannelRepository.findUserByPseudo(user, channel);
    if(u != null){
      u.setScore(u.getScore() + 1);
    }
    return u;    
  }
}
