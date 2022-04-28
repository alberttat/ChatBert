
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public static Socket socket;
	public static String username;

	public static void main(String[] args) throws IOException {
		System.out.println("Coming from Client");
		try {
			System.out.println("Attempting Socket Connection");

			socket = new Socket("127.0.0.1", 8443);
			// input reader from Server socket
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// output writer to server socket
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			Scanner input = new Scanner(System.in);
			System.out.print("Enter Username:");
			username = input.nextLine();

			// writes to output buffer
			writer.write(username);
			// sends data over socket, removes from buffer
			writer.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			socket.close();

			System.out.println("Connection Closed");
		}

		System.out.println("Username is: " + username);
	}
}
