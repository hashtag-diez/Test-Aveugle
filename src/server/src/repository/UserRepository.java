package server.src.repository;

import server.src.model.User;

public class UserRepository {

    public static void scored(User user){
        user.setScore(user.getScore() + 1 );
    }

    public static User createAndConnectUser(String channelName, String pseudo){
        User u = new User(pseudo);
        ChannelRepository.addParticipant(u, channelName);
        return u;
    }
    
}
