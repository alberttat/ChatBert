import java.io.IOException;
import java.util.ArrayList;

public class ClientHandler extends Thread {
	public Client currentClient;
	static ArrayList<Client> clientList = new ArrayList<Client>();

	public ClientHandler(Client serverClient) throws IOException {
		currentClient = serverClient;
		clientList.add(currentClient);
		System.out.println("size of array is: " + clientList.size());
	}

	public String getUsers() {
		String collectiveUsers = "Current users in server: ";
		for (Client i : clientList) {
			collectiveUsers = collectiveUsers + " " + i.getUsername();
		}
		return collectiveUsers;
	}

	public void sendAll(String toSend) throws IOException {
		for (Client i : clientList) {
			i.getSenderStream().println(currentClient.getUsername() + ":" + toSend);
		}
	}

	@Override
	public void run() {
		String input = "";
		boolean invalidUsername = true;
		boolean currentClientIsActive = true;
		while (currentClientIsActive && currentClient != null) {

			try {
				if (currentClient.getUsername() == null) {
					currentClient.getSenderStream().println("Please enter your username");
					while (invalidUsername) {
						if (currentClient.setUsername(currentClient.getReceiverStream().readLine())) {
							invalidUsername = false;
							currentClient.getSenderStream()
									.println("Welcome to ChatBert " + currentClient.getUsername());
							currentClient.getSenderStream().println("!help for some commands");
						} else {
							currentClient.getSenderStream().println("Please enter a valid username");
						}
					}
				}

				// read input from client
				input = currentClient.getReceiverStream().readLine();
				if (input.contains("!quit")) {
					currentClient.close();
					currentClientIsActive = false;
					Server.numClients--;
					clientList.remove(currentClient);
					System.out.println(getUsers());
				} else if (input.contains("!users")) {
					currentClient.getSenderStream().println(getUsers());
				} else if (input.contains("!help")) {
					currentClient.getSenderStream().println("Helpful commands" + "\n" + "!quit to disconnect" + "\n"
							+ "!help" + "\n" + "!weather" + "\n" + "!youtube" + "\n" + "!spotify");
				} else if (input.contains("!weather")) {
					currentClient.getSenderStream().println("Please enter your US zipcode");
					input = currentClient.getReceiverStream().readLine();
					APIHandler.weather(input);
					currentClient.getSenderStream().println(APIHandler.parseWeatherResponse(APIHandler.response));
				} else if (input.contains("!youtube")) {
					currentClient.getSenderStream().println("Please enter your youtube search");
					input = currentClient.getReceiverStream().readLine();
					APIHandler.youtube(input);
					currentClient.getSenderStream().println(APIHandler.parseYoutubeResponse(APIHandler.response));
				} else if (input.contains("!spotify")) {
					currentClient.getSenderStream().println("Please enter your spotify track search");
					input = currentClient.getReceiverStream().readLine();
					APIHandler.spotify(input);
					currentClient.getSenderStream().println(APIHandler.parseSpotifyResponse(APIHandler.response));
				} else {
					sendAll(input);
				}
			} catch (InterruptedException | IOException e) {
				System.out.println(currentClient.getUsername() + " has disconnected server side");
				currentClient = null;
			}
		}

	}

}
