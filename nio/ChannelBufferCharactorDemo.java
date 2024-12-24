package com.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class ChannelBufferCharactorDemo {
	public static void main(String[] args) throws IOException {
		
		
		RandomAccessFile file = new RandomAccessFile("/home/administrator/Downloads/abc.txt","r");
		FileChannel fileChannel = file.getChannel();
	
		ByteBuffer byteBuffer = ByteBuffer.allocate(48);
		
		Charset charset = Charset.forName("UTF-8");
		CharsetDecoder decoder = charset.newDecoder();
		
		while(fileChannel.read(byteBuffer) > 0) {
			// Prepare the buffer for reading
			byteBuffer.flip();
			
			CharBuffer charBuffer = decoder.decode(byteBuffer);
			
			System.out.print(charBuffer.toString());
			// Prepare the buffer for writing
			byteBuffer.clear();
		}
		
	}
}
