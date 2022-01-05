package server.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.Model.Channel;
import app.Model.Message;
import app.Model.Request;
import app.Model.Response;
import app.Model.Type;
import app.Model.User;
import app.utils.SerializationUtils;

public class appTest {

  private volatile String line; 
  public Main(String line) {
    this.line = line;
  }
  public void waitForResponse(AsynchronousSocketChannel socket, String line) throws 
    InterruptedException, ExecutionException, ClassNotFoundException, IOException{
    while(true){
      ByteBuffer buffer = ByteBuffer.allocate(1024);
      System.out.println("Nouvelle réponse !");
      socket.read(buffer).get();
      handleResponse(buffer);
    }
  }
  public Request outputParser(String line){
    Request req = new Request();
    Map<String, String> params = new HashMap<String, String>();
    String[] command = line.split(" ");
    switch(command[0]){
      case "signin":
        System.out.println("On veut se connecter au serveur !");
        req.setType(Type.SIGNIN);
        params.put("username", command[1]);
        params.put("password", command[2]);
        req.setParams(params);
        break;
      case "signup":
        System.out.println("On veut s'inscrire au serveur !");
        req.setType(Type.SIGNUP);
        params.put("username", command[1]);
        params.put("password", command[2]);
        req.setParams(params);
        break;
      case "signout":
        System.out.println("On veut se déconnecter du serveur !");
        req.setType(Type.SIGNOUT);
        break;
      case "create_channel":
        System.out.println("On veut créer un channel !");
        req.setType(Type.CREATE_CHANNEL);
        params.put("name", command[1]);
        params.put("admin", command[2]);     
        req.setParams(params);
        break; 
      case "create_message":
        System.out.println("On veut envoyer un message !");
        req.setType(Type.CREATE_MESSAGE);
        params.put("message", String.join(" ",Arrays.copyOfRange(command, 3, command.length)));
        params.put("username", command[1]);
        params.put("cuid", command[2]);
        req.setParams(params);
        break;   
      case "get_channels":
        System.out.println("On veut les channels");
        req.setType(Type.GET_CHANNELS);
        break;
      case "get_messages":
        System.out.println("On veut les messages");
        req.setType(Type.GET_MESSAGES);
      break;
      case "get_users":
        System.out.println("On veut les users");
        req.setType(Type.GET_USERS);
        break;
      case "delete_channel":
        System.out.println("On veut supprimer un channel.");
        req.setType(Type.DELETE_CHANNEL);
        params.put("cuid", command[1]);
        params.put("admin", command[2]);
        req.setParams(params);
        break;
      default:
        req.setType(Type.ANY);
        params.put("message", String.join(" ", command));
        req.setParams(params);
        break;
    }
    return req;
  }
  public void handleRequest(AsynchronousSocketChannel socket, String line, Scanner scanner) throws 
    InterruptedException, ExecutionException, IOException, ClassNotFoundException{
      while(!line.equals("Bye !")){
        line = scanner.nextLine();
        Request req = outputParser(line);
        socket.write(ByteBuffer.wrap(SerializationUtils.serializeRequest(req))).get();
      }
    sendDisconnectRequest(socket);
    scanner.close();
  }
  @SuppressWarnings("unchecked")
  public void handleResponse(ByteBuffer buffer) throws 
    ClassNotFoundException, IOException{
      Response response = SerializationUtils.deserializeResponse(buffer.flip().array());
      System.out.println(response.getType());
      if(response.getStatus().equals(Type.OK)){
        switch(response.getType()){
          case ANY:
            System.out.println("On a reçu un écho");
            String echoMessage = (String) response.getObj();
            System.out.println(echoMessage);
            break;
          case CREATE_CHANNEL:
            Channel newChannel = (Channel) response.getObj();
            System.out.println("Un nouveau channel a été créé ! " + newChannel.toString());
            break;
          case GET_CHANNELS:
            System.out.println("On a recu la liste des channels :");
            List<Channel> channels = (List<Channel>) response.getObj();
            for(Channel chan : channels){
              System.out.println(chan.toString());
            }
            break;
          case GET_USERS:
            System.out.println("Users connectés :");
            List<User> users = (List<User>) response.getObj();
            for(User user : users){
              System.out.println(user.toString());
            }
            break;
          case CREATE_MESSAGE:
            System.out.println("On a recu un nouveau message :");
            Message message = (Message) response.getObj();
            System.out.println(message.toString());
            break;
          case GET_MESSAGES:
            System.out.println("On a recu la liste des messages :");
            List<Message> messages = (List<Message>) response.getObj();
            for(Message mess : messages){
              System.out.println(mess.toString());
            }
            break;
          case SIGNIN:
            System.out.println("Trouvé !");
            User user = (User) response.getObj();
            System.out.println("Salut " + user.getUsername() + " !");
            break;
          case SIGNOUT:
            User disconnectedUser = (User) response.getObj();
            System.out.println("Bye " + disconnectedUser.getUsername() + " !");
            break;
          case SIGNUP:
            User newUser = (User) response.getObj();
            System.out.println("Bienvenue " + newUser.getUsername() + " !");
            break;
          case DELETE_CHANNEL:
            int cuid = (int) response.getObj();
            System.out.println("Deleted channel with id" + cuid);
            break;
          default:
            break; 
        }
      } else {
        System.out.println("Erreur");
      }
  }
  public void sendDisconnectRequest(AsynchronousSocketChannel socket) throws 
    InterruptedException, ExecutionException, IOException{
      Request req = new Request();
      req.setType(Type.EXIT);
      req.setParams(null);
      socket.write(ByteBuffer.wrap(SerializationUtils.serializeRequest(req))).get();
  }
  public void setShutdownHook(AsynchronousSocketChannel socket){
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
          try {
            Thread.sleep(200);
            sendDisconnectRequest(socket);
          } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              e.printStackTrace();
          } catch (ExecutionException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
      }
    });
  }
  public void run() throws 
    InterruptedException, ExecutionException, ClassNotFoundException, IOException {
      try(AsynchronousSocketChannel socket = AsynchronousSocketChannel.open()) {
      socket.connect(new InetSocketAddress("localhost", 1237)).get();
      Scanner scanner = new Scanner(System.in);
      ExecutorService pool = Executors.newCachedThreadPool();
      pool.submit(() -> {
        try {
          waitForResponse(socket, line);
        } catch (ClassNotFoundException | InterruptedException | ExecutionException | IOException e) {
          e.printStackTrace();
        }
      });
      setShutdownHook(socket);
      handleRequest(socket, line, scanner);
    }
	}
  public static void main(String[] args) throws ClassNotFoundException, InterruptedException, ExecutionException, IOException {
    new Main("").run();
  }
}
