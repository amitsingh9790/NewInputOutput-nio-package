package com.nio;

import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Scanner;

class Shared{
	static int flag;
}
class SendingMessage extends Thread {
	IPtuxEx ip;
	SocketChannel socketchannel;
	SendingMessage(IPtuxEx ip,SocketChannel socketchannel){
		this.ip=ip;
		this.socketchannel = socketchannel;
	}
	
	ByteBuffer bb = ByteBuffer.allocate(256);

	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		String msg = "";
		do {
			System.out.println("enter message:");
			try {
				msg = sc.nextLine();
				bb.put(msg.getBytes());
				bb.flip();
				while (bb.hasRemaining()) {
					socketchannel.write(bb);
				}
				bb.clear();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} while (!msg.equals("stop"));
		Shared.flag=1;
			try {
				String sip = socketchannel.getRemoteAddress().toString();
				sip = sip.substring(1, 14);
				socketchannel.close();
//				IPtuxEx ip=new IPtuxEx();
				
				ip.ipMap.remove(sip);
				ip.showMenu();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}

}

class Recievemessage extends Thread {

	public Recievemessage(SocketChannel socketchannel) {

		this.socketchannel = socketchannel;
	}

	ServerSocketChannel serversocketchannel;
	SocketChannel socketchannel;

	@Override
	public void run() {
		// create a server socket channel
		// Register the new connection for reading
		int byteread = 0;
		String response = "";
		try {
			System.out.println("accepted new connection from " + socketchannel.getRemoteAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scanner sc = new Scanner(System.in);
		ByteBuffer bb = ByteBuffer.allocate(256);
		int bytesRead;

		do {
			response = "";
			if(!socketchannel.isOpen()||Shared.flag==1) 
				break;
			// Read data from client
			try {
				bytesRead = socketchannel.read(bb);
				bb.flip();
				if (bytesRead != -1) {
					response = new String(bb.array(), 0, bytesRead);
					System.out.println("recieved from server:" + response);
				}
				bb.clear();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (!response.equals("stop"));
		Shared.flag=0;
	}
}

public class IPtuxEx {
	final static int PORT_NUMBER = 3300;
	HashMap<String, SocketChannel> ipMap = new HashMap<String, SocketChannel>();
	String thisIp;

	public IPtuxEx() throws UnknownHostException {
		thisIp = InetAddress.getLocalHost().getHostAddress();
	}

	void connectToClient() throws IOException {
		// Create a server socket channel
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress("192.168.5.206", PORT_NUMBER));
		while (true) {
			try {
				SocketChannel clientChannel = serverSocketChannel.accept();
				// Register the new connection for reading
				System.out.println("Accepted new connection from " + clientChannel.getRemoteAddress());
				String ip = clientChannel.getRemoteAddress().toString();
				ip = ip.substring(1, 14);
				System.out.println(ip);
				if(ipMap.get(ip) == null) {
					ipMap.put(ip, clientChannel);
				}
				System.out.println(ipMap.get(ip).isOpen());
//			ipMap.put(clientChannel.getRemoteAddress().toString(), clientChannel);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} 

	void showMenu() {
			System.out.println("Choose IPs are..\nEnter 0 to quit");
			for (String ip : ipMap.keySet()) {
				System.out.println(ip);
			}
			String selectedIp = new java.util.Scanner(System.in).nextLine();
			if (selectedIp.equals("0")) {
				System.exit(0);
			}
			SocketChannel selectedSocket = ipMap.get(selectedIp);
//			System.out.println("kk:"+selectedSocket==null);
			
			SendingMessage sm = new SendingMessage(this,selectedSocket);
			sm.start();
//
			Recievemessage rm = new Recievemessage(selectedSocket);
			rm.start();
			System.out.println("====================================");
	}

	public static void main(String[] args) throws IOException {
		IPtuxEx ipmain = new IPtuxEx();
		new Thread(() -> {
			try {
				ipmain.connectToClient();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
//	new Thread(() -> {
//		try {
//			ipmain.connectToServer();
//		} catch (UnknownHostException | InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}).start();
		ipmain.showMenu();

	}
}