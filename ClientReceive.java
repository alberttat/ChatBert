import java.io.IOException;

public class ClientReceive extends Thread {
	// public BufferedReader receiver;
	public Client client;

	public ClientReceive(Client client) throws IOException {
		this.client = client;

	}

	@Override
	public void run() {

		while (client != null) {
			// read messages from server socket

			try {
				System.out.print(">");
				System.out.println(client.getReceiverStream().readLine());

			} catch (IOException e) {
				client = null;
			}

		}

	}
}
