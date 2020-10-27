/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import dao.LocationDAO;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.BatoiLogicLocation;

public class ConnectionThread extends Thread {

    private DatagramPacket clientDatagramPacket;

    public ConnectionThread(DatagramPacket clientDatagramPacket)
    {
        this.clientDatagramPacket = clientDatagramPacket;
    }
    
    @Override
    public void run()
    {
        try
        {
            ByteBuffer buffer = ByteBuffer.wrap(this.clientDatagramPacket.getData());
            
            int id = buffer.getInt();
            double lat = buffer.getDouble();
            double lon = buffer.getDouble();

            new LocationDAO().insert(new BatoiLogicLocation(id, lat, lon));
            System.out.println("Inserted new location");

            synchronized (this)
            {
                DatagramSocket socket = new DatagramSocket();
                Employees.get().forEach(e -> {
                    try
                    {
                        // Letting know all employees that have to refresh the map.
                        System.out.println("Sending to "+e.getAddress());
                        socket.send(new DatagramPacket(new byte[0],0, e.getAddress(),e.getPort()));
                    }
                    catch (IOException ex)
                    {
                        ex.printStackTrace();
                        Employees.get().remove(e);
                    }
                });
            }
        } catch (Exception ex) {
            Logger.getLogger(ConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}