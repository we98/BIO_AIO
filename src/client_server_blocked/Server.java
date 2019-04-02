package client_server_blocked;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        ExecutorService tp = Executors.newCachedThreadPool();
        try {
            serverSocket = new ServerSocket(8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true){
            try {
                clientSocket = serverSocket.accept();
                System.out.println(clientSocket.getRemoteSocketAddress() + " connected!");
                tp.execute(new HandleMsg(clientSocket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static class HandleMsg implements Runnable{
        private Socket client;
        HandleMsg(Socket clientSocket){
            client = clientSocket;
        }
        @Override
        public void run() {
            BufferedReader bufferedReader = null;
            PrintWriter printWriter = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String inputLine = null;
                printWriter = new PrintWriter(client.getOutputStream(), true);
                while((inputLine = bufferedReader.readLine()) != null){
                    System.out.println(client.getRemoteSocketAddress() + " send message: " + inputLine);
                    printWriter.println(inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                printWriter.close();
            }
        }
    }
}
