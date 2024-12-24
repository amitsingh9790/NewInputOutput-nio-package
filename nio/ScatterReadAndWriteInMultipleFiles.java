package com.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ScatterReadAndWriteInMultipleFiles {
	public static void main(String[] args) throws IOException {
		FileInputStream source = new FileInputStream("/home/administrator/Downloads/xyz.txt");//Path of source
		FileChannel sourceChannel = source.getChannel();
		
		FileOutputStream dest1 = new FileOutputStream("/home/administrator/Downloads/abc.txt");
		FileOutputStream dest2 = new FileOutputStream("/home/administrator/Downloads/abc2.txt");
		FileOutputStream dest3 = new FileOutputStream("/home/administrator/Downloads/abc3.txt");
		
		FileChannel destChannel1 = dest1.getChannel();
		FileChannel destChannel2 = dest2.getChannel();
		FileChannel destChannel3 = dest3.getChannel();
		
		ByteBuffer b1 = ByteBuffer.allocate(4);
		ByteBuffer b2 = ByteBuffer.allocate(9);
		ByteBuffer b3 = ByteBuffer.allocate(2);
		
		FileChannel[] channelArray = {destChannel1,destChannel2,destChannel3};
		ByteBuffer[] bufferArray = { b1, b2, b3 };
		while(sourceChannel.read(bufferArray) >0) {
			for(int i=0; i<3; i++) {
				bufferArray[i].flip();
				channelArray[i].write(bufferArray[i]);
				bufferArray[i].flip();
			}
			
		}
		
	}

}
