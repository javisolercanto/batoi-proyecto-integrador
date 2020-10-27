package server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerTCP
{
    private static final int PORT = 8888;

    public static void main(String[] args)
    {
        try(ServerSocket serverSocket = new ServerSocket(PORT))
        {
            while (true)
                new ServerThread(serverSocket.accept()).start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}