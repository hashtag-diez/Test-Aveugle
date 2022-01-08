package src.network;

import java.io.IOException;

import java.nio.channels.AsynchronousSocketChannel;
import src.utils.Serialization;

import src.model.Load;
import src.model.Type;
import src.model.Status;
import src.model.Channel;



import java.nio.ByteBuffer;

public class UserConnection {
  private volatile String line;
  private static AsynchronousSocketChannel socket;

  public UserConnection(String line) {
    this.line = line;
  }

  public void waitForResponse(AsynchronousSocketChannel socket, String line)
      throws InterruptedException, ExecutionException, ClassNotFoundException, IOException {
    while (true) {
      ByteBuffer buffer = ByteBuffer.allocate(1024);
      socket.read(buffer).get();
      handleResponse(buffer);
    }
  }

  public void handleResponse(ByteBuffer buffer) throws ClassNotFoundException, IOException {
    Load response = Serialization.deserializeMap(buffer.flip().array());
    if (response.getStatus().equals(Status.OK)) {
      switch (response.getType()) {
        case CHANNEL_CREATE:
          Channel newChannel = response.;
          System.out.println("Création de " + newChannel.getName() + "(" + newChannel.getCuid() + ")");
          Network.receiveChannel(newChannel);
          break;
        case GET_CHANNELS:
          List<Channel> channels = (List<Channel>) response.getObj();
          Network.updateChannelList(channels);
          break;
        case GET_USERS:
          List<User> users = (List<User>) response.getObj();
          Network.updateUserList(users);
          break;
        case CREATE_MESSAGE:
          Message message = (Message) response.getObj();
          System.out.println(message.getUsername() + " " + message.getMessage() + " " + message.getCuid());
          Network.receiveMessage(message);
          break;
        case GET_MESSAGES:er, avec juste la connexion au socket à gérer en plus
          List<Message> messages = (List<Message>) response.getObj();
          Network.updateMessageList(messages);
          break;
        case SIGNIN:
        case SIGNUP:
          User user = (User) response.getObj();
          System.out.println("Connexion de " + user.getUsername());
          if ((Network.isSigninRequestInProcess() || Network.isSignupRequestInProcess()))
            Network.setCurrentUser(user);
          else
            Network.addUser(user);
          break;
        case SIGNOUT:
          User disconnectedUser = (User) response.getObj();
          System.out.println("Déconnexion de " + disconnectedUser.getUsername());
          Network.setSignout(disconnectedUser);
          break;
        case DELETE_CHANNEL:
          int cuid = (int) response.getObj();
          System.out.println("Suppression de " + cuid);
          Network.receiveDeleteChannel(cuid);
          break;
        default:
          break;
      }
    } else {
      System.out.println("Erreur");
      Network.setAllRequestError();
    }
  }

}
