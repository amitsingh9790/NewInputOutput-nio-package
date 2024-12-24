package com.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class ChannelTransferUsingStream {
	public static void transferTo() throws IOException {
		FileInputStream  fis = new FileInputStream("/home/administrator/Downloads/abc.txt");
		FileOutputStream fos = new FileOutputStream("/home/administrator/Downloads/xyz.txt");
		
		FileChannel sourceChannel = fis.getChannel();
		FileChannel destChannel =  fos.getChannel();
		//Transfer data from source to destination channel
		long size = sourceChannel.size();
		sourceChannel.transferTo(0, size, destChannel);
		
		System.out.print("File copy completed successfully!)");
		
	}
	public static void transferFrom() throws IOException {
		FileInputStream  fis = new FileInputStream("/home/administrator/Downloads/abc.txt");
		FileOutputStream fos= new FileOutputStream("/home/administrator/Downloads/xyz.txt");
		
		FileChannel fromChannel = fis.getChannel();
		FileChannel toChannel =  fos.getChannel();
		
		long positon = 0;
		long count = fromChannel.size();
		toChannel.transferFrom(fromChannel, positon, count);
		System.out.println("File copy completed successfully!");
	}
	public static void main(String[] args) throws IOException {
		//File Copying using Stream
//		transferTo();
		transferFrom();
	}
}
