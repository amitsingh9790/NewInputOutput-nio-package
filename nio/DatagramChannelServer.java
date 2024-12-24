package com.nio;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class DatagramChannelServer {
    public static void main(String[] args) throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(9999));

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        System.out.println("Waiting for a message...");
        
        SocketAddress senderAddress = channel.receive(buffer);
        buffer.flip();

        String message = new String(buffer.array(), 0, buffer.limit());
        System.out.println("Received message: " + message + " from " + senderAddress);

        buffer.clear();
        buffer.put("Response from Amit server".getBytes());
        buffer.flip();
        channel.send(buffer, senderAddress);

        channel.close();
    }
}
