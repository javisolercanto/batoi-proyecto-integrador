package dam.android.sergic.app2.asyncTasks;

import android.content.Context;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import dam.android.sergic.app2.models.Order;

public class UpdateStatus extends AsyncFather
{
    private Order toUpdate;

    private static final String SERVER = "grup2.cipfpbatoi.es";
    private static final int PORT = 8888;
    private static final int TIME_OUT = 5000;

    public UpdateStatus(Context context, AsyncResponse response, Order orderBack)
    {
        super(context, response);
        this.toUpdate = orderBack;
    }

    @Override
    protected ArrayList<?> doInBackground(Object... objects)
    {
        ArrayList<String> results = null;

        if(super.isNetworkAvailable())
        {
            try(Socket socket = new Socket())
            {
                socket.setSoTimeout(TIME_OUT);
                socket.connect(new InetSocketAddress(SERVER,PORT));

                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                DataInputStream input = new DataInputStream(socket.getInputStream());
                results = new ArrayList<>();

                output.writeUTF(toUpdate.getOrderNumber());
                output.writeInt(toUpdate.getStatus());

                if(toUpdate.getStatus()==Order.NOT_DELIVERED)
                    output.writeUTF(toUpdate.getAbout());

                results.add(input.readUTF());

                output.close();
                input.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return results;
    }
}