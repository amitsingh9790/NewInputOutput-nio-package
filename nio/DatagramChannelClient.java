package com.nio;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class DatagramChannelClient {
    public static void main(String[] args) throws IOException {
    	DatagramChannel clientChannel = DatagramChannel.open();

//    	ByteBuffer buffer = ByteBuffer.wrap("Hello, Server".getBytes());
    	ByteBuffer buffer = ByteBuffer.allocate(1024);
    	InetSocketAddress serverAddress = new InetSocketAddress("192.168.5.195", 9999);
    	clientChannel.send(buffer, serverAddress);

    	// Optionally receive a response from the server
    	buffer.clear();
    	clientChannel.receive(buffer);
    	buffer.flip();
    	String response = new String(buffer.array(), 0, buffer.limit());
    	System.out.println("Response from server: " + response);
    	clientChannel.close();
    }
}
