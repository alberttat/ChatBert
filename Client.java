
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class Client {
	private static String HOMEIP = "127.0.0.1";
	private static int PORT = 8443;
	private Socket socket;
	private String username;
	private BufferedReader clientInputStream;
	private BufferedReader receiverStream;
	private PrintWriter senderStream;

	public Client() throws IOException {
		setSocket(new Socket(HOMEIP, PORT));
		clientInputStream = (new BufferedReader(new InputStreamReader(System.in)));
		receiverStream = (new BufferedReader(new InputStreamReader(getSocket().getInputStream())));
		senderStream = (new PrintWriter(getSocket().getOutputStream(), true));
	}

	public Client(String username) throws IOException {
		this.username = username;
		setSocket(new Socket(HOMEIP, PORT));
		clientInputStream = (new BufferedReader(new InputStreamReader(System.in)));
		receiverStream = (new BufferedReader(new InputStreamReader(getSocket().getInputStream())));
		senderStream = (new PrintWriter(getSocket().getOutputStream(), true));
	}

	public Client(Socket clientSocket) throws IOException {
		setSocket(clientSocket);
		clientInputStream = (new BufferedReader(new InputStreamReader(System.in)));
		receiverStream = (new BufferedReader(new InputStreamReader(getSocket().getInputStream())));
		senderStream = (new PrintWriter(getSocket().getOutputStream(), true));
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public boolean setUsername(String username) {
		String regex = "^[a-zA-Z]+$";
		if (username.matches(regex)) {
			this.username = username;
			return true;
		}
		return false;

	}

	public String getUsername() {
		return this.username;
	}

	public BufferedReader getClientInputStream() {
		return clientInputStream;
	}

	public BufferedReader getReceiverStream() {
		return receiverStream;
	}

	public PrintWriter getSenderStream() {
		return senderStream;
	}

	public void close() throws IOException {
		getSocket().close();
		getClientInputStream().close();
		getReceiverStream().close();

		System.out.println(getSocket().toString() + " - closed");
	}

	public static void main(String[] args) throws IOException {

		Client client = new Client();

		System.out.println("Attempting Socket Connection");
		ClientReceive receive = new ClientReceive(client);
		receive.start();

		while (client != null) {
			try {

				// read input from client and send over socket
				String inputString = client.getClientInputStream().readLine();
				System.out.println(">");
				if (inputString.equals("!quit")) {
					client.close();
					Server.numClients--;
					System.out.println(Server.numClients);
					break;
				} else {
					client.getSenderStream().println(inputString);
				}
			} catch (SocketException exception) {
				System.out.println("Connection closed on client side");
				client.close();
			}
		}
	}

}
