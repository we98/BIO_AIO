package client_server_blocked;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
public class Client {
    public static void main(String[] args) {
        Socket client = null;
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        try {
            client = new Socket();
            client.connect(new InetSocketAddress("localhost", 8000));
            printWriter = new PrintWriter(client.getOutputStream(), true);
            String inputLine = null;
            Scanner scanner = new Scanner(System.in);
            while(!"".equals((inputLine = scanner.nextLine()))){
                printWriter.println(inputLine);
                printWriter.flush();
                bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                System.out.println("From sever: " + bufferedReader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            printWriter.close();
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
