package client_server_nonblocked;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;


public class Client {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8000));
        ByteBuffer writeBuffer = ByteBuffer.allocate(128);
        Scanner scanner = new Scanner(System.in);
        String inputLine = null;
        while(!"".equals(inputLine = scanner.nextLine())){
            writeBuffer.clear();
            byte[] bs = inputLine.getBytes();
            for(int i = 0; i < bs.length; i++){
                writeBuffer.put(bs[i]);
            }
            writeBuffer.flip();
            socketChannel.write(writeBuffer);
        }
    }
}