package server.src.repository;

import server.src.model.User;

public class UserRepository {

    public static void scored(String user){
        User u = findUserByPseudo(user, channel);
        u.setScore(user.getScore() + 1 );
    }

    public static User createAndConnectUser(String channelName, String pseudo){
        User u = new User(pseudo);
        addParticipant(u, channelName);
        return u;
    }

    // TODO: discuss the place of this function
    public static boolean disconnectUser(String user, String channel){
        
        return true;
        return false;
    }
    
}
