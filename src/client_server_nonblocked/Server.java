package client_server_nonblocked;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
public class Server {
    public static void main(String[] args) {
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress("localhost", 8000));
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true){
            try{
                selector.select();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                if(key.isAcceptable()){
                    try{
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println(socketChannel.getRemoteAddress() + " connected!");
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                else if(key.isReadable()){
                    SocketChannel socketChannel = (SocketChannel)key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                    try{
                        socketChannel.read(byteBuffer);
                        byteBuffer.flip();
                        System.out.println(socketChannel.getRemoteAddress() + " send message: " + new String(byteBuffer.array()).trim());
                    }
                    catch (IOException e){
                        key.cancel();
                        try{
                            socketChannel.socket().close();
                            socketChannel.close();
                        }
                        catch (IOException e1){}
                    }
                    try{
                        socketChannel.register(selector, SelectionKey.OP_WRITE);
                    }
                    catch (ClosedChannelException e){

                    }
                }
                else if(key.isWritable()){
                    SocketChannel channel = (SocketChannel)key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                    try{
                        byte[] bs = "Server received successfully!".getBytes();
                        for(int i = 0; i < bs.length; i++){
                            byteBuffer.put(bs[i]);
                        }
                        channel.write(byteBuffer);
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                    try{
                        channel.register(selector, SelectionKey.OP_READ);
                    }
                    catch (ClosedChannelException e){}
                }
                iterator.remove();
            }
        }
    }
}