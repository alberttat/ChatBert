import java.io.IOException;

public class ClientReceive extends Thread {
	// public BufferedReader receiver;
	public Client client;

	public ClientReceive(Client client) throws IOException {
		this.client = client;
		// receiver = new BufferedReader(new
		// InputStreamReader(client.socket.getInputStream()));
	}

	@Override
	public void run() {

		try {
			while (true) {
				// read messages from server socket
				System.out.println(client.getReceiverStream().readLine());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
