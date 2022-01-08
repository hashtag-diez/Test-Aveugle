package src.repository;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.List;

import src.App;
import src.model.Categorie;
import src.model.Channel;
import src.model.User;

public class ChannelRepository {

    public static User findUserByPseudo(String name, String channel){
        Channel c = getChannelByName(channel);
        if(c == null) return null;
        for(User u : c.getChannelParticipants()){
            System.out.println(u.getPseudo());
            if(u.getPseudo().equals(name)) return u;
        }
        return null;
    }
    
    public static void addParticipant(User user, String channel){
        Channel c = getChannelByName(channel);
        if(c != null && user != null) {
            List<User> lU = c.getChannelParticipants();
            lU.add(user);
            c.setChannelParticipants(lU);
        }
    }

    public static boolean removeParticipant(String username , String channel){
        Channel c = getChannelByName(channel);
        if(username!=null && c!=null) {
            System.out.println("Hop");
            c.removeUser(username);
            return true;
        }
        return false;
    }

    public static Channel addChannel(String channel , String adminName, String categorie, int nbTours, AsynchronousSocketChannel client){
        User user = new User(adminName, client);
        Categorie categ = CatalogueRepository.findCategorieByName(App.catalogue, categorie);
        System.out.println(categ.getCategoryName());
        Channel c = new Channel(channel, user, categ, nbTours);
        App.rooms.add(c);
        return c;
    }

    public static boolean deleteChannel(String channel, String userName){
        for(Channel c : App.rooms){
            if(c.getChannelName().equals(channel)){
                App.rooms.remove(c);
                return true;
            }
        }
        return false;
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
            if(c.getChannelName().equals(channelName)) return c;
        }
        System.out.println("Rien trouvé");
        return null;
    }
}
