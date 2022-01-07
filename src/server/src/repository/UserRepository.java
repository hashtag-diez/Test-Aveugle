package server.src.repository;

import server.src.model.User;

public class UserRepository {

    public static void scored(User user){
        user.setScore(user.getScore() + 1 );
    }
    
}
