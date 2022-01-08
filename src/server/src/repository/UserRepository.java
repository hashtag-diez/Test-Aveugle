package src.repository;

import src.model.User;

public class UserRepository {

    public static User createAndConnectUser(String pseudo, String channelName){
        User user = new User(pseudo);
        ChannelRepository.addParticipant(user, channelName);
        return user;
    }

    public static boolean disconnectUser(String pseudo, String channelName) {
        User user = ChannelRepository.findUserByPseudo(pseudo, channelName);
        System.out.println(user==null);
        return ChannelRepository.removeParticipant(pseudo, channelName);
    }
}
