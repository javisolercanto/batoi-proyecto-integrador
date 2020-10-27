package dam.android.sergic.app2.asyncTasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public abstract class AsyncFather extends AsyncTask<Object, Void, ArrayList<?>>
{
    protected WeakReference<Context> appContext;
    private WeakReference<ProgressBar> progress;
    protected AsyncResponse listener;

    // -------------------------- INTERFACE - Listener Pattern ------------------
    public interface AsyncResponse
    {
        void processFinish(ArrayList<?> output);
    }
    //---------------------------------------------------------------------------

    protected AsyncFather(Context context, ProgressBar pb, AsyncResponse response)
    {
        this.appContext = new WeakReference<>(context);
        this.progress = new WeakReference<>(pb);
        this.listener = response;
    }

    protected AsyncFather(Context context, AsyncResponse response)
    {
        this.appContext = new WeakReference<>(context);
        this.listener = response;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        if(progress!=null) progress.get().setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(ArrayList<?> objects)
    {
        super.onPostExecute(objects);
        if(progress!=null) progress.get().setVisibility(View.GONE);
        listener.processFinish(objects);
    }

    @Override
    protected void onCancelled()
    {
        super.onCancelled();
        if(progress!=null) progress.get().setVisibility(View.GONE);
    }

    // check if network is available and if there is connectivity
    protected boolean isNetworkAvailable()
    {
        boolean networkAvailable = false;

        ConnectivityManager cm = (ConnectivityManager) appContext.get().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null)
        {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isConnected())
                networkAvailable = true;
        }

        return networkAvailable;
    }
}