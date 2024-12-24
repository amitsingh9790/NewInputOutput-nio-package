package com.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ScatterReadFromChannelDemo {
	public static void main(String args[]) throws IOException{
		FileInputStream source = new FileInputStream("/home/administrator/Downloads/xyz.txt");//Path of source
		FileChannel fileChannel = source.getChannel();
		
		ByteBuffer c1b = ByteBuffer.allocate(4);
		ByteBuffer c2b = ByteBuffer.allocate(9);
		ByteBuffer c3b = ByteBuffer.allocate(2);
		
		ByteBuffer[] bufferArray = { c1b, c2b, c3b };
		while(fileChannel.read(bufferArray) >0 ) {
			for(ByteBuffer buf:bufferArray) {
				buf.flip();
				while(buf.hasRemaining()) {
					System.out.print((char) buf.get());
				}
				System.out.println("===");
				buf.clear();
			}
		}
		}
}
