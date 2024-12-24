package com.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class E8_ServerSocketBlockingDemo {
	public static void main(String[] args) throws IOException, InterruptedException {

		// Create a server socket channel
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress("192.168.5.206", 3300));
		//serverSocketChannel.configureBlocking(false); //this create non blocking mode
		SocketChannel clientChannel = serverSocketChannel.accept();
		
		// Register the new connection for reading	
		System.out.println("Accepted new connection from " +
		clientChannel.getRemoteAddress());
		// Read data from the client
		ByteBuffer buffer = ByteBuffer.allocate(256);
		
		while(clientChannel.read(buffer)>0) {
		buffer.flip();
		System.out.println("Msg from client::");
		while (buffer.hasRemaining()) {
            System.out.print((char) buffer.get());
         }
		buffer.clear();
		String msg =  new java.util.Scanner(System.in).nextLine();
		buffer.put(msg.getBytes());
		buffer.flip();
		clientChannel.write(buffer);
		}
	}
}
