
import java.io.IOException;
import java.net.ServerSocket;

public class Server {

	private int PORT = 8443;
	private ServerSocket serverSocket;
	private static int numClients = 0;

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
				ClientHandler something = new ClientHandler(serverClient);
				something.start();
			}
		} catch (IOException e) {

		} finally {
			server.close();
			System.out.println("Connection Closed");
		}

	}
}
