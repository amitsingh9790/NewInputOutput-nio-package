package com.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class E8_ClientSocketDemo {
    public static void main(String[] args) {
        // Create a new socket channel
        try (SocketChannel socketChannel = SocketChannel.open()) {
            // Connect to the server at localhost on port 8080
            boolean isConnected= socketChannel.connect(new InetSocketAddress("192.168.5.187",1234));
            System.out.println("Is server available:"+isConnected);
            // Prepare a message to send
            String message = "Hello, Server!";
            ByteBuffer buffer = ByteBuffer.allocate(256);
            buffer.put(message.getBytes());
            buffer.flip();
            // Send the message to the server
            while (buffer.hasRemaining()) { 
                socketChannel.write(buffer);
            }
            // Clear the buffer and prepare to receive a response
            buffer.clear();
            
            int bytesRead = socketChannel.read(buffer);
            // Print the server's response
            if (bytesRead != -1) {
                String response = new String(buffer.array(), 0, bytesRead);
                System.out.println("Received from server: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
