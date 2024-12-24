package com.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelBufferDemo {
	public static void main(String[] args) throws IOException {
		RandomAccessFile file = new RandomAccessFile("/home/administrator/Downloads/tv.deb","r");
		FileChannel fileChannel = file.getChannel();
		ByteBuffer byteBuffer = ByteBuffer.allocate(48);
		while(fileChannel.read(byteBuffer) > 0) {
			//file buffer ot prepare for get operation 
			byteBuffer.clear();
			while(byteBuffer.hasRemaining()) {
				System.out.print(byteBuffer.getChar());
			}
			byteBuffer.clear();
		}

	}
}
