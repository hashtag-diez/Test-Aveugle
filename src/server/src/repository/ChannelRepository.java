package server.src.repository;

import java.util.List;

import server.src.App;
import server.src.model.Channel;
import server.src.model.User;

public class ChannelRepository {

    public static User findUserByPseudo(String name, String channel){
        Channel c = getChannelByName(channel);
        if(c == null) return null;
        for(User u : c.getChannelParticipants()){
            if(u.getPseudo() == name) return u;
        }
        return null;
    }
    
    public static void addParticipant(String userName, String channel){
        //TODO : findUser
        Channel c = getChannelByName(channel);
        
        if(c == null) return;
        
    }
    public static void removeParticipant(String user, String channel){
        User u = findUserByPseudo(user, channel);
        Channel c = getChannelByName(channel);
        if(u!=null && c!=null) {
            List<User> lU = c.getChannelParticipants();
            lU.remove(u);
            c.setChannelParticipants(lU);
        }
    }
    public static void clearUserList(String channel){
        Channel c = getChannelByName(channel);
        if(c!=null) {
            List<User> lU = c.getChannelParticipants();
            lU.clear();;
            c.setChannelParticipants(lU);
        }
    }

    public static void start(String channel){
        Channel c = getChannelByName(channel);
        if(c!=null) {
            c.setIsStarted(true);;
        }
    }

    public static Channel getChannelByName(String channelName){
        for(Channel c : App.rooms){
            if(c.getChannelName() == channelName) return c;
        }
        return null;
    }
}
