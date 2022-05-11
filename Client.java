
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	private String HOMEIP = "127.0.0.1";
	private int PORT = 8443;
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

	public void setUsername(String username) {
		this.username = username;
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

	public String close() throws IOException {
		getSocket().close();
		return getSocket().toString() + " - closed";
	}

	public static void main(String[] args) throws IOException {

		Client client = new Client();

		System.out.println("Coming from Client");
		System.out.println("Attempting Socket Connection");
		ClientReceive receive = new ClientReceive(client);
		receive.start();

		while (true) {

			System.out.println(">");

			// read input from client and send over socket
			String inputString = client.getClientInputStream().readLine();

			if (inputString.equals("quit"))
				break;
			client.getSenderStream().println(inputString);

		}
		client.close();
		System.out.println("Connection Closed");

	}

}
