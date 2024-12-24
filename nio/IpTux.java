package com.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

public class IpTux {

	final static int PORT_NUMBER = 3300;
	HashMap<String, SocketChannel> ipMap = new HashMap<String, SocketChannel>();
	String thisIp;
 
	public IpTux() throws UnknownHostException {
		thisIp = InetAddress.getLocalHost().getHostAddress();
	}

	void connectToClient() {
		// Create a server socket channel
		try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
			System.out.println("-------------------------------------------------------");
			serverSocketChannel.bind(new InetSocketAddress(PORT_NUMBER));
			SocketChannel clientChannel = serverSocketChannel.accept();
			// Register the new connection for reading
			System.out.println("Accepted new connection from " + clientChannel.getRemoteAddress());
			String ip = clientChannel.getRemoteAddress().toString();
			ip = ip.substring(1,14);
			System.out.print(ip);
			ipMap.put(ip, clientChannel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void connectToServer() throws UnknownHostException, InterruptedException {
		while (true) {
			for(int i= 155; i< 213; i++) {
				String ip = "192.168.5" +"." +i;
//			for (String ip : ipMap.keySet()) {
				if (ip.equals(thisIp)) {
					continue;
				}
				try (SocketChannel socketChannel = SocketChannel.open()) {
					boolean isConnected = socketChannel.connect(new InetSocketAddress(ip, PORT_NUMBER));
					if (isConnected) {
						System.out.println("::::"+socketChannel.getRemoteAddress().toString());
						ip = socketChannel.getRemoteAddress().toString();
						ip = ip.substring(1,14);
						System.out.println(ip);
						ipMap.put(ip, socketChannel);

					} else {
						System.out.println(ip + " server not available");
					}

				} catch (IOException e) {
					if (e.getMessage().startsWith("Connection timed out") == false) {
						e.printStackTrace();
					}
				}
			}
			Thread.sleep(5000);
		}
	}

	void showMenu() {
		while (true) {
			System.out.println("Choose IPs are..\nEnter 0 to quit");
			for (String ip : ipMap.keySet()) {
				System.out.println(ip);
			}
			String selectedIp = new java.util.Scanner(System.in).nextLine();
			if (selectedIp.equals("0")) {
				System.exit(0);
			}
			SocketChannel selectedSocket = ipMap.get(selectedIp);
			startCommunication(selectedSocket);
		}
	}

	void startCommunication(SocketChannel selectedSocket) {
		try {
			String message = "Hello, from " + thisIp;
			ByteBuffer buffer = ByteBuffer.allocate(256);
			do {
				buffer.put(message.getBytes());
				buffer.flip();
				// Send the message to the server
//				System.out.println(buffer);
				while (buffer.hasRemaining()) {
					selectedSocket.write(buffer);
				}
				// Clear the buffer and prepare to receive a response
				buffer.clear();
				int bytesRead = selectedSocket.read(buffer);
				// Print the server's response
				if (bytesRead != -1) {
					message = new String(buffer.array(), 0, bytesRead);
					System.out.println("Received: " + message);
				}
				System.out.println("Enter your msg: (bye to quit)");
				message = new java.util.Scanner(System.in).nextLine();
			} while (!message.equals("bye"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getResponsiveIPs(String subnet, int timeout) throws InterruptedException, IOException {
		while (true) {
			for (int i = 155; i < 213; i++) { // Iterate over 155-213
				String ip = subnet + "." + i;
//				InetAddress inet = InetAddress.getByName(ip);
//				if (inet.isReachable(timeout)) {
					ipMap.put(ip, null);
//				} else {
//					ipMap.remove(ip);
//				}
			}
			Thread.sleep(5000);
		}
	}

	public static void main(String[] args) {
		IpTux obj;
		try {
			obj = new IpTux();

			System.out.println("waiting for client");
//			new Thread(() -> {
//				try {
//					obj.getResponsiveIPs("192.168.5", 1000);
//				} catch (InterruptedException | IOException e) {
//					e.printStackTrace();
//				}
//			}).start();
			new Thread(() -> obj.connectToClient()).start();
			System.out.println("trying to connect to server");
			new Thread(() -> {
				try {
					obj.connectToServer();
				} catch (UnknownHostException | InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
			obj.showMenu();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}