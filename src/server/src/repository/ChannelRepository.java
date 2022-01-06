package server.src.repository;

import server.src.model.Channel;
import server.src.model.User;

public class ChannelRepository {

    public static User findUserByPseudo(String name, Channel channel){
        for(User u : channel.getChannelParticipants()){
            if(u.getPseudo() == name) return u;
        }
        return null;
    }
    
}
