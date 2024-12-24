package com.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;


public class ServerSocketBlockingDemo {
	public static void main(String[] args) throws IOException {
		
		//Create a server socket channel
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress("192.168.5.206",3300));
		SocketChannel clientChannel = serverSocketChannel.accept();
		
//		Register the new connection for reading 
		System.out.println("Accepted new connection from ");
//		clientChannel.getRemoteAddress();tring msg = "This is my message"; 
		Scanner sc = new Scanner(System.in);
		String msg = "";
		String servermsg= "";
		do{
			//Read data from the client 
			ByteBuffer buffer = ByteBuffer.allocate(256);
			int bytesRead = clientChannel.read(buffer);
			buffer.flip();
			System.out.println("Msg from client::");

			servermsg="";
			while(buffer.hasRemaining()) {
				char val = (char)buffer.get();
				System.out.print(val);
				servermsg = servermsg+Character.toString(val);
			}
			if(servermsg.equals("stop"))
				break;
			
			buffer.clear();
			
			System.out.println("Enter your message: \n");
			msg = sc.nextLine();
			
			buffer.put(msg.getBytes());
			buffer.flip();
			clientChannel.write(buffer);	
		}while(!msg.equals("stop"));
		
		
		
	}
}
