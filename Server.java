
import java.io.IOException;
import java.net.ServerSocket;
import java.net.http.HttpClient;

public class Server {

	private int PORT = 8443;
	private ServerSocket serverSocket;
	static int numClients = 0;
	static HttpClient client = HttpClient.newHttpClient();

	public Server() throws IOException {
		serverSocket = new ServerSocket(PORT);
		System.out.println("Server Online");
	}

	public String close() throws IOException {
		serverSocket.close();
		return serverSocket.toString() + " - closed";
	}

	public static void main(String[] args) throws IOException {

		Server server = new Server();

		try {
			while (true) {
				Client serverClient = new Client(server.serverSocket.accept());
				numClients++;
				System.out.println(numClients + " Client connected");
				ClientHandler clientPool = new ClientHandler(serverClient);
				clientPool.start();
			}
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			server.close();
			System.out.println("Server connection Closed");
		}

	}
}
