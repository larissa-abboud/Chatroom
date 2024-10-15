import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

	private ArrayList<ConnectionHandler> connections;
	private ServerSocket server;
	private boolean done;
	private ExecutorService pool;

	public Server() {
		connections = new ArrayList<>();
		done = false;
	}

	public void run() {
		try {
			server = new ServerSocket(9999);
			pool = Executors.newCachedThreadPool();
			while (!done) {
				Socket client = server.accept();
				ConnectionHandler handler = new ConnectionHandler(client);
				connections.add(handler);
				pool.execute(handler);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void broadcast(String message) {
		for (ConnectionHandler handler : connections) {
			if (handler != null) {
				handler.sendMessage(message);
			}
		}
	}

	public void shutdown() {
		try {
			done = true;
			pool.shutdown();
			if (!server.isClosed()) {
				server.close();
			}
			for (ConnectionHandler handler : connections) {
				handler.shutdown();
			}
		} catch (IOException e) {
			// we will just ignore the error;
		}

	}

	class ConnectionHandler implements Runnable {

		private Socket client;
		private BufferedReader in;
		private PrintWriter out;
		private String name;

		public ConnectionHandler(Socket client) {
			this.client = client;

		}

		public void run() {
			try {
				out = new PrintWriter(client.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				out.println("Please enter your name: ");
				name = in.readLine();
				System.out.println(name + " Connected!!");
				broadcast(name + " Joined the chat");
				String message;
				while ((message = in.readLine()) != null) {
					if (message.startsWith("/nick ")) {
						String[] messageSplit = message.split(" ", 2);
						if (messageSplit.length == 2) {
							broadcast(name + " renamed themselves to " + messageSplit[1]);
							System.out.println(name + " renamed themselves to " + messageSplit[1]);
							name = messageSplit[1];
							out.println("Name changed successfully to " + name);
						} else {
							out.println("No name provided");
						}
					} else if (message.startsWith("/quit")) {
						broadcast(name + " left the chat");
						shutdown();
					} else {
						broadcast(name + ": " + message);
					}
				}
			} catch (IOException e) {
				shutdown();
			}

		}

		public void sendMessage(String message) {
			out.println(message);
		}

		public void shutdown() {
			try {
				in.close();
				out.close();
				if (!client.isClosed()) {
					client.close();
				}
			} catch (IOException e) {

			}

		}

	}
	public static void main(String[] args) {
		Server server = new Server();
		server.run();
		
	}
}
