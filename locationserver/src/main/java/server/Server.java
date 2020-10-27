/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class Server
{
    private static final int PORT = 8888;
    private static final int BUFFER_SIZE = 20;
    private static final int EMPLOYEE = -1;                     // If you are the employee app.
    
    public static void main(String[] args)
    {
        try
        {
            DatagramSocket socket = new DatagramSocket(PORT);

            ByteBuffer reader;
            int result;
            while (true)
            {
                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket clientDatagramPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(clientDatagramPacket);

                reader = ByteBuffer.wrap(clientDatagramPacket.getData());
                result = reader.getInt();
                if(result != EMPLOYEE)
                {
                    System.out.println("Phone connected");
                    new ConnectionThread(clientDatagramPacket).start();
                }
                else
                {
                    if(Employees.isHere(clientDatagramPacket))
                    {
                        System.out.println("Employee Disconnected");
                        Employees.delete(clientDatagramPacket);
                    }
                    else
                    {
                        System.out.println("Employee Connected");
                        Employees.add(clientDatagramPacket);
                    }
                }
            }
        }
        catch (SocketException ex)
        {
            System.err.println("SERVER -> CONNECTION NOT AVAILABLE");
        } catch (IOException ex) {
            System.err.println("SERVER -> ERROR RECEIVING THE PACKET");
        }
    }
}