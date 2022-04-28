
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	private String users[];
	public static ServerSocket serverSocket;
	public static Socket clientSocket;

	public String[] getUsers() {
		return users;
	}

	public void setUsers(String users[]) {
		this.users = users;
	}

	public void run() {

	}

	public static void main(String[] args) throws IOException {

		try {
			serverSocket = new ServerSocket(8443);
			System.out.println("Server Online");

			clientSocket = serverSocket.accept();

			System.out.println("Client connected");
			BufferedReader read = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			System.out.println(read.readLine());

		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			serverSocket.close();
			clientSocket.close();
			System.out.println("Connection Closed");
		}

	}

}
