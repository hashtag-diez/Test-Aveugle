package src;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import src.model.*;
/* import router.Router;
 */import src.utils.Serialization;

public class App implements Callable<Boolean> {
	private AsynchronousServerSocketChannel server;

	public static Catalogue catalogue = new Catalogue();
	// TODO: correct related bugs
	public static List<AsynchronousSocketChannel> clients = new ArrayList<AsynchronousSocketChannel>();
	public static List<Channel> rooms = new ArrayList<Channel>();
	public Map<User, Channel> players = new HashMap<User, Channel>();
	
/* 	private Router router;
	public App() throws Exception{
			router = new Router();
	} */
	public void handleRequest(Load request, Load response, AsynchronousSocketChannel client) throws 
		InterruptedException, ExecutionException, IOException{
			if(request.getType().equals(Type.USER_CONNECT)){
				response.setType(request.getType());
				response.setRange(Range.ONLY_CLIENT);
				response.setStatus(Status.OK);
				Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
				Map<String, String> res = new HashMap<String, String>();
				res.put("message", request.getData().get("params").get("username"));
				data.put("result", res);
				response.setData(data);
			};
			sendToOneClient(response, client);
	}
	public void waitForRequest(AsynchronousSocketChannel client) throws 
		InterruptedException, ExecutionException, IOException, ClassNotFoundException{
			while(true){
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				client.read(buffer).get();
				Load request = Serialization.deserializeLoad(buffer.flip().array(), true);
				Load response = new Load();
				handleRequest(request, response, client);
			}
	}
	public void sendToAll(Load res) throws InterruptedException, ExecutionException, IOException{
		// Faire sur la liste de users à la place
    for (AsynchronousSocketChannel client : clients) {
			client.write(ByteBuffer.wrap(Serialization.serializeLoad(res))).get();
		}
	}
	/* public void sendToPlayers(Load res, AsynchronousSocketChannel client) throws InterruptedException, ExecutionException, IOException{
    for (Map.Entry<Channel, List<User>> entry : App.rooms.entrySet()) {
			List<User> players = entry.getValue();
			if(players.contains(App.users.get(client))){
				for(User user : players){
					user.getClientSocket().write(ByteBuffer.wrap(Serialization.serializeLoad(res))).get();
				}
			}
		}
	} */
	public void sendToOneClient(Load res, AsynchronousSocketChannel client) throws IOException, InterruptedException, ExecutionException{
		client.write(ByteBuffer.wrap(Serialization.serializeLoad(res))).get();
	}
	@Override
	public Boolean call() throws Exception {
		server = AsynchronousServerSocketChannel.open();
		server.bind(new InetSocketAddress("localhost", 1237));
		server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
			@Override
			public void completed(AsynchronousSocketChannel client, Object attachment) {
				server.accept(null, this);
				try{
					System.out.println("Un client s'est connecté depuis " + client.getRemoteAddress());
				} catch (Exception e) {
					failed(e, null);
				}
				try {
					waitForRequest(client);
				} catch (Exception e) {
					failed(e, null);
				}
			}
			@Override
			public void failed(Throwable exc, Object attachment) {
				exc.printStackTrace();
			}
		});
		System.in.read();
		return true;
	}
	public static void main(String[] args) throws Exception {
		new App().call();
	}
}
