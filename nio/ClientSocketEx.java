package com.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

class SendMessage extends Thread{
	private SocketChannel socketChannel = null;
	ByteBuffer buffer = ByteBuffer.allocate(256);
	String message = "";
	
	public SendMessage(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		try{
			
			do {
				message = sc.nextLine();
				buffer.put(message.getBytes());
				buffer.flip();

				while (buffer.hasRemaining()) {
					socketChannel.write(buffer);

				}
				buffer.clear();
			} while (!message.equals("stop"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.close();
		}
	}
	
}
 
class RecieveMessage extends Thread{
	SocketChannel socketChannel = null;
	ByteBuffer buffer = ByteBuffer.allocate(256);
	String response = "";

	public RecieveMessage(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	@Override
	public void run() {
		int bytesRead=0;
		do {
			try {
				bytesRead = socketChannel.read(buffer);
				buffer.flip();
				if (bytesRead != -1) {
					response = new String(buffer.array(), 0, bytesRead);
					System.out.println("recieved from server:" + response);
				}
				buffer.clear();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}while(!response.equals("stop"));
	}
}

public class ClientSocketEx {
	public static void main(String[] args) {
		
		try{
			SocketChannel socketChannel = SocketChannel.open();
			boolean isConnected = socketChannel.connect(new InetSocketAddress("192.168.5.195", 3300));
			System.out.println("is server available:" + isConnected);

				SendMessage sendMessage = new SendMessage(socketChannel);
				sendMessage.start();
				
				RecieveMessage recieveMessage = new RecieveMessage(socketChannel);
				recieveMessage.start();
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}