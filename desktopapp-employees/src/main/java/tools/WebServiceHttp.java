package tools;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.BatoiLogicDeliveryNote;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

public class WebServiceHttp extends Thread
{
    // Constants
    public static final int LAT = 0;
    public static final int LON = 1;

    private OnMyRequestComplete listener;
    private ObservableList<BatoiLogicDeliveryNote> notes;


    // ------------------ Interface ----------------------
    public interface OnMyRequestComplete
    {
        void requestComplete(double[] latLong);
    }
    // ---------------------------------------------------

    public WebServiceHttp(ObservableList<BatoiLogicDeliveryNote> notes, OnMyRequestComplete listener)
    {
        this.notes = FXCollections.observableArrayList(notes);
        this.listener = listener;
    }

    @Override
    public void run()
    {
        super.run();
        AtomicReference<URL> url = new AtomicReference<>();
        AtomicReference<String> urlOpenStreetMap = new AtomicReference<>();
        notes.forEach(n ->
        {
            urlOpenStreetMap.set("https://nominatim.openstreetmap.org/search/" + n.getBatoiLogicOrder().getBatoiLogicAddress().toString()
                    + "?format=json&addressdetails=1&limit=1&polygon_svg=1");

            try
            {
                url.set(new URL(urlOpenStreetMap.toString()));
                JSONArray jsonArray = getObjectJson(url.get());

                if(jsonArray!=null)
                {
                    JSONObject place = jsonArray.getJSONObject(0);
                    double[] result = new double[2];
                    result[LAT] = place.getDouble("lat");
                    result[LON] = place.getDouble("lon");

                    if(!isInterrupted())
                        listener.requestComplete(result);
                }
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
        });
    }

    protected JSONArray getObjectJson(URL url)
    {
        HttpURLConnection conn = null;
        JSONArray jsonArray = null;

        try
        {
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            // test if response was OK (response = 200)
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                // read data stream from url
                String resultStream = readStream(conn.getInputStream());

                // create JSON array object
                jsonArray = new JSONArray(resultStream);
            }
            else
                System.out.println("Error connection "+conn.getResponseMessage());
        }
        catch (IOException | JSONException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(conn != null)
                conn.disconnect();
        }

        return jsonArray;
    }

    protected String readStream(InputStream inputStream)
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            String nextLine;
            while ((nextLine = reader.readLine())!=null)
                sb.append(nextLine);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return sb.toString();
    }
}