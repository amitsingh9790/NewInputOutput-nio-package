package com.nio.transferwithnetwork;

import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ClientSocketSendFile2Ex {
	public static void main(String[] args) {
		try (SocketChannel socketChannel = SocketChannel.open()) {
			boolean isConnected = socketChannel.connect(new InetSocketAddress("192.168.5.195", 3300));
			System.out.println("is server available:" + isConnected);

			String message = null;
			String response = null;
			do {
				message="";
//				Scanner sc = new Scanner(System.in);
//				message = sc.nextLine();
				RandomAccessFile file = new RandomAccessFile("/home/administrator/Downloads/myFile.txt","r");
				FileChannel fileChannel = file.getChannel();
				ByteBuffer buffer = ByteBuffer.allocate(23);
				while(fileChannel.read(buffer) > 0) {
					buffer.clear();
					while(buffer.hasRemaining()) {
						char val = (char)buffer.get();
						System.out.print(val);
						message = message+Character.toString(val);
					}
					
					buffer.clear();
				}
//				buffer.clear();
				System.out.println(message.getBytes().length);
				buffer = ByteBuffer.allocate(message.getBytes().length);
				buffer.put(message.getBytes());
				buffer.flip();
				while (buffer.hasRemaining()) {
					socketChannel.write(buffer);

				}
				buffer.clear();
				
				int bytesRead = socketChannel.read(buffer);

				if (bytesRead != -1) {
					response = new String(buffer.array(), 0, bytesRead);
					System.out.println("recieved from server:" + response);
				}
				buffer.clear();// 1
			} while (!response.equals("stop"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
