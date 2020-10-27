package server;

import db.OrderDAO;
import models.Order;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ServerThread extends Thread
{
    private Socket socket;

    public ServerThread(Socket accept)
    {
        this.socket = accept;
    }

    @Override
    public void run()
    {
        super.run();

        try(DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream()))
        {
            String orderNumber = input.readUTF();
            int status = input.readInt();
            String about = null;

            if(status == Order.NOT_DELIVERED)
                about = input.readUTF();

            OrderDAO dao = new OrderDAO();
            dao.update(dao.findByName(orderNumber), status+"", about);

            // Returning the ID to say that everything is FINE.
            output.writeUTF(orderNumber);

            socket.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}