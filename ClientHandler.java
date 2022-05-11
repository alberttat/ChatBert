import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
	private String users[];
	public Client currentClient;
	public Socket clientSocket;
	static ArrayList<Client> clientList = new ArrayList<Client>();

	public ClientHandler(Client serverClient) throws IOException {
		clientSocket = serverClient.getSocket();
		currentClient = serverClient;
		clientList.add(currentClient);
		System.out.println(clientList.toArray());
		System.out.println("size of array is: " + clientList.size());
	}

	public String[] getUsers() {
		return users;
	}

	public void setUsers(String users[]) {
		this.users = users;
	}

	public String close() throws IOException {
		clientSocket.close();
		return clientSocket.toString() + " - closed";
	}

	public void sendAll(String toSend) throws IOException {
		for (Client i : clientList) {
			i.getSenderStream().println(currentClient.getUsername() + ":" + toSend);
			// i.sender.println(toSend);
		}

	}

	@Override
	public void run() {

		try {
			currentClient.getSenderStream().println("Please enter your username");
			String username = currentClient.getReceiverStream().readLine();
			currentClient.setUsername(username);
			while (true) {
				// read input from client
				String input = currentClient.getReceiverStream().readLine();
				// currentClient.sender.println("Server:" + new Date().toString() + ":" +
				// input);
				sendAll(input);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
