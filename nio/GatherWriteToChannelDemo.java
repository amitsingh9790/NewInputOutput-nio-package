package com.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class GatherWriteToChannelDemo {
	public static void main(String[] args) throws IOException {
		FileInputStream inputFile1 = new FileInputStream("/home/administrator/Downloads/abc1.txt");
		FileInputStream inputFile2 = new FileInputStream("/home/administrator/Downloads/abc2.txt");
		
		FileOutputStream outputFile = new FileOutputStream("/home/administrator/Downloads/abc.txt");
		
		FileChannel channelsrc1 = inputFile1.getChannel();
		FileChannel channelsrc2 = inputFile2.getChannel();
		FileChannel channelDest = outputFile.getChannel();
		
		
		ByteBuffer byteBuffer1 = ByteBuffer.allocate(128);
		ByteBuffer byteBuffer2 = ByteBuffer.allocate(128);
		
		channelsrc1.read(byteBuffer1);
		channelsrc2.read(byteBuffer2);
		
		ByteBuffer[] bufferArray = { byteBuffer1,byteBuffer2 };
		
		for(ByteBuffer  buf: bufferArray) {
			buf.flip();
		}
		channelDest.write(bufferArray);
		for(ByteBuffer buf:bufferArray) {
			buf.clear();
		}
		
	}
}
