package com.nio;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class VoiceChatClient {
    public static void main(String[] args) {
        try (SocketChannel clientChannel = SocketChannel.open(new InetSocketAddress("192.168.5.195", 3300))) {
            System.out.println("Connected to the server...");
            capturingAndSendingAudio(clientChannel);
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
   static void capturingAndSendingAudio(SocketChannel clientChannel) throws IOException, LineUnavailableException {
		TargetDataLine microphone;
		AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
		microphone = AudioSystem.getTargetDataLine(format);
		microphone.open(format);
		microphone.start();

		byte[] buffer = new byte[1024];
		int bytesRead;

		while ((bytesRead = microphone.read(buffer, 0, buffer.length)) > 0) {
		    ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, 0, bytesRead);
		    clientChannel.write(byteBuffer);
		}

	}
}
