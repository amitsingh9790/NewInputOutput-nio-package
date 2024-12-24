package com.nio.transferwithnetwork;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.util.Scanner;
//Better aproch for sending file
public class ServerSideRecieveFileEx {
	public static void main(String[] args) {
		try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()){
			serverSocketChannel.bind(new InetSocketAddress("192.168.5.206",3300));
			System.out.println("Server is listening on port 3300...");
			
			try(SocketChannel socketChannel = serverSocketChannel.accept()){
				System.out.println("Connection accepted from:"+socketChannel.getRemoteAddress());
				try(FileChannel fileChannel = FileChannel.open((Paths.get("eassay.txt")))){
					long fileSize = fileChannel.size();
					ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
					buffer.putLong(fileSize);
					buffer.flip();
					socketChannel.write(buffer); //Send file size first
					
					fileChannel.transferTo(0, fileSize, socketChannel); //Send the file
					System.out.println("File sent successfully.");
				}catch(IOException e) {
					System.err.print("Error handling client: "+e.getMessage());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
