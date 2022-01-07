package src.repository;

import src.model.Channel;
import src.model.User;

public class ChannelRepository {

    public static User findUserByPseudo(String name, Channel channel){
        for(User u : channel.getChannelParticipants()){
            if(u.getPseudo() == name) return u;
        }
        return null;
    }
    
}
