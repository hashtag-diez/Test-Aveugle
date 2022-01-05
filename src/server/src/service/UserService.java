package server.src.service;

import server.src.service.serviceinterface.ServiceInterface;

//import App

public class UserService implements ServiceInterface {
  // private UserRepository UserRepository

  public UserService(Connection con) throws Exception{
    try{
      //this.UserRepository = new UserRepository(con);
    }catch(SQLException e){
      throw e;
    }
  }

  public void enterPseudo(AsynchronousSocketChannel client) throws NoSuchAlgorithmException, SQLException{
    String pseudo = getParams().get("pseudo");
    if(pseudo==""){
      // setStatus(Type.ERROR)
    }else{
      //User user = UserRepository.findByUsername(username);
      if(App.users.containsValue(user)){
        //setStatus(Type.ERROR);
        //setObj("Entrer un autre pseudo"); -> do it with other method
      }else{
        // App.users.put(client, user)
        // setStatus(Type.OK)
        // return smth to frontend
      }
    }
  }

  public void run(AsynchronousSocketChannel client) throws IOException, NoSuchAlgorithmException, SQLException {
    switch(req.getType()){
      case ENTER_PSEUDO:
        System.out.println("Un client entre son pseudo et se connecte !");
        enterPseudo(client);
        break;
      default:
        break;
    }
  }

}