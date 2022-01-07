package server.src.model;

import java.util.ArrayList;
import java.util.List;

public class Channel {
    private String name;
    private User admin;
    private List<User> participants;
    private boolean isStarted;

    public Channel(String name, User admin){
        this.name = name;
        this.admin = admin;
        this.isStarted = false;
        this.participants = new ArrayList<>();
        participants.add(admin);
    }

    public String getChannelName(){return name;}

    public User getChannelAdmin(){return admin;}
    
    public List<User> getChannelParticipants(){return participants;}

    public void setChannelName(String name){
        this.name = name;
    }

    public void setChannelAdmin(User admin){
        this.admin = admin;
    }
    
    public void setChannelParticipants(List<User> participants){
        this.participants = participants;
    }

    public boolean getIsStarted(){
        return isStarted;
    }

    public void setIsStarted(boolean b){
        this.isStarted = b;
    }
    

}
