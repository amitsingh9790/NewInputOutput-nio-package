package com.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
//Write the comments
public class ChannelBuffercopying {
	public static void main(String[] args) throws IOException {
		RandomAccessFile file = new RandomAccessFile("/home/administrator/Downloads/abc.txt", "r");
		RandomAccessFile file2 = new RandomAccessFile("/home/administrator/Downloads/def.txt", "rw");
		
		//Get channel of file 1
		FileChannel fileChannel = file.getChannel();
		
		FileChannel fileChannel2 = file2.getChannel();
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(48);
		while(fileChannel.read(byteBuffer) > 0 ) {
			byteBuffer.flip();
			fileChannel2.write(byteBuffer);
			byteBuffer.clear();
			System.out.println(byteBuffer);
		}
		file.close();
		file2.close();
	}
}
