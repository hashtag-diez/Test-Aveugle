package src.model;

import java.util.ArrayList;
import java.util.List;

public class Channel {
    private String name;
    private User admin;
    private List<User> participants;
    private int cuid;
    private Categorie categorie;

    public Channel(String name, User admin){
        this.name = name;
        this.admin = admin;
        this.participants = new ArrayList<>();
        participants.add(admin);
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getChannelName(){return name;}

    public User getChannelAdmin(){return admin;}
    
    public List<User> getChannelParticipants(){return participants;}

    public void addParticipant(User u){
        participants.add(u);
    }
    public int getCuid() {
        return cuid;
    }

    public void setCuid(int cuid) {
        this.cuid = cuid;
    }

    public void removeParticipant(User u){
        participants.remove(u);
    }
    public void clearUserList(){
        participants.clear();;
    }

}
