import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class GameServer {

	private static ServerSocket serverSocket = null;

	private static Socket clientSocket = null;

	private static final int maxClientsCount = 2;
	private static final clientThread[] threads = new clientThread[maxClientsCount];

	public static LinkedBlockingDeque<int[][]> messages;

	public static void main(String args[]) {

		int portNumber = 2222;
		int[][] game_matrix_server_table;
		game_matrix_server_table = new int[20][20];
		game_matrix_server_table[10][10] = 5;
		messages = new LinkedBlockingDeque<int[][]>();

		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			System.out.println(e);
		}

		while (true) {
			try {
				clientSocket = serverSocket.accept();
				int i = 0;
				for (i = 0; i < maxClientsCount; i++) {
					if (threads[i] == null) {
						(threads[i] = new clientThread(clientSocket, threads)).start();
						break;
					}
				}

				if (i == maxClientsCount) {
					PrintStream os = new PrintStream(
							clientSocket.getOutputStream());
					os.println("Server too busy. Try later.");
					os.close();
					clientSocket.close();
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
}

class clientThread extends Thread {

	private String clientName = null;
	private ObjectInputStream object_input_stream = null;
	private DataOutputStream os = null;
	private ObjectOutputStream object_output_stream = null;
	private Socket clientSocket = null;
	public boolean game_player_ready;
	private final clientThread[] threads;
	private int maxClientsCount;

	private boolean player_one_ready = false;
	private boolean player_two_ready = false;
	private int state_counter = 0;

	public clientThread(Socket clientSocket, clientThread[] threads) {
		this.clientSocket = clientSocket;
		this.threads = threads;
		maxClientsCount = threads.length;
		this.game_player_ready = false;
	}

	public void run() {
		int maxClientsCount = this.maxClientsCount;
		clientThread[] threads = this.threads;

		try {

			this.object_output_stream = new ObjectOutputStream(
					clientSocket.getOutputStream());
			this.object_input_stream = new ObjectInputStream(
					clientSocket.getInputStream());

			String name;
			// GameTurnStatus game_turn_status;
			// GamePlayerReady game_player_ready;
			while (true) {

				switch (this.state_counter) {
				case 0:
					this.game_player_ready = (boolean) this.object_input_stream
							.readObject();
					System.out.println("Player Ready: "
							+ this.game_player_ready);
					if (this == threads[0]) {
						this.player_one_ready = true;
						this.state_counter++;
						System.out.println("Player ONE Ready!");
					}
					if (this == threads[1]) {
						this.player_two_ready = true;
						this.state_counter++;
						System.out.println("Player TWO Ready!");
					}
					break;
				case 1:
					GameServer.messages
							.addFirst((int[][]) this.object_input_stream
									.readObject());
					this.state_counter++;
					break;
				case 2:
					int[][] table;
					table = GameServer.messages.getFirst();

					System.out.println("SERVER [0][0] after readObject: "
							+ table[0][0]);
					System.out.println("SERVER [0][1] after readObject: "
							+ table[0][1]);
					System.out.println("SERVER [1][0] after readObject: "
							+ table[1][0]);
					System.out.println("SERVER [1][1] after readObject: "
							+ table[1][1]);

					if (this == threads[0]) {
						System.out.println("Sending GameMatrix To Player TWO!");
						threads[1].object_output_stream.writeObject(table);
						threads[1].object_output_stream.flush();
					}
					if (this == threads[1]) {
						System.out.println("Sending GameMatrix To Player ONE!");
						threads[0].object_output_stream.writeObject(table);
						threads[0].object_output_stream.flush();
					}
					table = null;
					this.state_counter--;
					break;
				}
			}
			// clientSocket.close();
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
