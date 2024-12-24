package com.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class VoiceChatServer {
	public static void main(String[] args) throws IOException, LineUnavailableException {
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress("192.168.5.206",3300));
		
		SocketChannel clientChannel = serverSocketChannel.accept();
		receivingAndPlayingAudio(clientChannel);
	}
	public static void receivingAndPlayingAudio(SocketChannel clientChannel) throws LineUnavailableException, IOException {
		SourceDataLine speakers;
		AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
		speakers = AudioSystem.getSourceDataLine(format);
		speakers.open(format);
		speakers.start();
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		while(clientChannel.read(buffer)>0) {
			buffer.flip();
			speakers.write(buffer.array(), 0, buffer.limit());
			 buffer.clear();
		}
	}

}
