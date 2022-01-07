package src.model;

import java.util.ArrayList;
import java.util.List;

public class Channel {
    private String name;
    private User admin;
    private List<User> participants;

    public Channel(String name, User admin){
        this.name = name;
        this.admin = admin;
        this.participants = new ArrayList<>();
        participants.add(admin);
    }

    public String getChannelName(){return name;}

    public User getChannelAdmin(){return admin;}
    
    public List<User> getChannelParticipants(){return participants;}

    public void addParticipant(User u){
        participants.add(u);
    }
    public void removeParticipant(User u){
        participants.remove(u);
    }
    public void clearUserList(){
        participants.clear();;
    }

}
