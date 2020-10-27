package tools;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class SocketLocation extends Thread
{
    private DatagramSocket socket;
    private MyMapListener listener;

    private static final String SERVER = "grup2.cipfpbatoi.es";
    private static final int PORT = 8888;
    private static final int HERE = -1;
    private final DatagramPacket packet;

    // --------------------- Interface --------------------------------
    public interface MyMapListener
    {
        void onMapChange();
    }
    // ----------------------------------------------------------------

    public SocketLocation(MyMapListener listener) throws UnknownHostException, SocketException
    {
        this.listener = listener;
        socket = new DatagramSocket();

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(HERE);
        packet = new DatagramPacket(buffer.array(),buffer.array().length, InetAddress.getByName(SERVER),PORT);
    }

    @Override
    public void run()
    {
        super.run();
        try
        {
            System.out.println("Sending message...");
            socket.send(packet);

            while (true)
            {
                System.out.println("Waiting until I have to refresh map.");
                socket.receive(packet);
                System.out.println("REFRESHING");

                synchronized (this)
                {
                    // Notify to the user interface there are changes in the map.
                    listener.onMapChange();
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Socket closed");
        }

        // Sending close message to server.
        try
        {
            socket = new DatagramSocket();
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.putInt(HERE);
            socket.send(new DatagramPacket(buffer.array(),buffer.array().length, InetAddress.getByName(SERVER),PORT));
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public synchronized DatagramSocket getSocket()
    {
        return socket;
    }
}