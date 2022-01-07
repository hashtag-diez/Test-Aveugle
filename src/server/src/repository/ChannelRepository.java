package server.src.repository;

import java.util.List;

import server.src.App;
import server.src.model.Categorie;
import server.src.model.Channel;
import server.src.model.User;

public class ChannelRepository {

    // TODO: discuss the place of this function
    public static User findUserByPseudo(String name, String channel){
        Channel c = getChannelByName(channel);
        if(c == null) return null;
        for(User u : c.getChannelParticipants()){
            if(u.getPseudo() == name) return u;
        }
        return null;
    }
    
    // TODO: discuss the place of this function
    public static void addParticipant(User user, String channel){
        Channel c = getChannelByName(channel);
        if(c != null && user != null) {
            List<User> lU = c.getChannelParticipants();
            lU.add(user);
            c.setChannelParticipants(lU);
        }
    }

    public static Channel addChannel(String channel , String adminName, String categorie){
        User user = new User(adminName);
        Categorie categ = CatalogueRepository.findCategorieByName(App.catalogue, categorie);
        Channel c = new Channel(channel, user, categ);
        App.rooms.add(c);
        return c;
    }

    public static boolean deleteChannel(String channel, String userName){
        Channel c = getChannelByName(channel);
        if(c!=null && c.getChannelAdmin().getPseudo() == userName) {
            App.rooms.remove(c);
            return true;
        }
        return false;
    }

    // TODO: discuss the place of this function
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
