package com.nio.transferwithnetwork;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ClientSideSendFileEx2 {
	public static void main(String[] args) throws IOException {
		try(SocketChannel socketChannel = SocketChannel.open((new InetSocketAddress("192.168.5.195",3300)))){
			System.out.println("Connected to the server...");
			ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
			socketChannel.read(buffer);
			buffer.flip();
			long fileSize = buffer.getLong();
			System.out.println("File size: "+ fileSize);
			try(FileChannel fileChannel = FileChannel.open(Paths.get("received_file.txt"),StandardOpenOption.CREATE, StandardOpenOption.WRITE)){
				long bytesTransferred =0;
				while(bytesTransferred < fileSize) {
					bytesTransferred += fileChannel.transferFrom(socketChannel, bytesTransferred, fileSize-bytesTransferred);
				}
			}
			System.out.println("File received successsfully.");
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
