package com.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class E9_SelectorServerSocketDemo {
    public static void main(String[] args) throws IOException {
        // Create a selector
        Selector selector = Selector.open();

        // Create a server socket channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8080));
        serverSocketChannel.configureBlocking(false);

        // Register the channel with the selector for accepting connections
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // Select keys, blocking until at least one channel is ready
            selector.select();

            // Get the set of ready keys
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    // Accept the new client connection
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    SocketChannel clientChannel = serverChannel.accept();
                    clientChannel.configureBlocking(false);

                    // Register the new connection for reading
                    clientChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("Accepted new connection from " + clientChannel.getRemoteAddress());
                } else if (key.isReadable()) {
                    // Read data from the client
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    int bytesRead = clientChannel.read(buffer);

                    if (bytesRead == -1) {
                        // Client closed the connection
                        clientChannel.close();
                    } else {
                        // Echo the data back to the client
                        buffer.flip();
                        String msgFromClient = new String(buffer.array(), 0, bytesRead);
		                System.out.println("Received from : " +clientChannel.getRemoteAddress()+":="+ msgFromClient);
                        clientChannel.write(buffer);
                        buffer.clear();
                    }
                }
            }
        }
    }
}
