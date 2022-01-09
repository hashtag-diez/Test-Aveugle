package src.model;

import java.util.ArrayList;
import java.util.List;

public class Channel {
    

    private String name;
    private User admin;
    private List<User> participants;
    private List<String> answeredQuestions;
    private Categorie categorie;
    private boolean isStarted;
    private boolean found; 

    public Channel(String name, User admin, Categorie categorie){
        this.name = name;
        this.admin = admin;
        this.isStarted = false;
        this.categorie = categorie;
        this.participants = new ArrayList<>();
        answeredQuestions = new ArrayList<String> ();
        participants.add(admin);
        this.found = false;
    }
    public void addUser(User user){
        participants.add(user);
    }
    public boolean isFound() {
        return found;
    }
    public void setFound(boolean found) {
        this.found = found;
    }
    public List<String> getAnsweredQuestions() {
        return answeredQuestions;
    }
    public void addQuestions(String question) {
        this.answeredQuestions.add(question);
    }
    public boolean removeUser(String username){
        for(User user : participants){
            System.out.println("User : "+user.getPseudo());
            if(user.getPseudo().equals(username)){  
                System.out.println("Trouvé !");
                participants.remove(user);
                return true;
            } 
        }
        return false;
    }
    public String getChannelName(){return name;}

    public User getChannelAdmin(){return admin;}
    
    public List<User> getChannelParticipants(){return participants;}

    public void setChannelName(String name){
        this.name = name;
    }

    public Categorie getCategorie(){return categorie;}

    public void setCategorie(Categorie categ){
        this.categorie = categ;
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
