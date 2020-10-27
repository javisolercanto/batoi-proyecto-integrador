package dam.android.sergic.app2.asyncTasks;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import dam.android.sergic.app2.models.Order;

public class GetLocationNames extends AsyncFather
{
    private WeakReference<ArrayList<Order>> orders;

    public GetLocationNames(Context context, AsyncResponse response, ArrayList<Order> orders)
    {
        super(context, response);
        this.orders = new WeakReference<>(orders);
    }

    @Override
    protected ArrayList<?> doInBackground(Object... objects)
    {
        ArrayList<LatLng> actualAddresses = null;

        try
        {
            if(super.isNetworkAvailable())
            {
                Geocoder coder = new Geocoder(appContext.get(), Locale.getDefault());
                actualAddresses = new ArrayList<>();

                String address;
                List<Address> addresses;
                for(Order o : orders.get())
                {
                    address = o.getAddress().toString();
                    addresses = coder.getFromLocationName(address, 1);

                    if(addresses.size()>0)
                    {
                        actualAddresses.add(new LatLng(addresses.get(0).getLatitude(),
                                addresses.get(0).getLongitude()));
                    }
                    else
                        actualAddresses.add(null);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return actualAddresses;
    }
}