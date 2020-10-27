package dam.android.sergic.app2.asyncTasks;

import android.content.Context;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Set;
import dam.android.sergic.app2.dao.DeliveryNoteDAO;
import dam.android.sergic.app2.dao.RouteDAO;
import dam.android.sergic.app2.models.DeliveryNote;
import dam.android.sergic.app2.models.Route;
import dam.android.sergic.app2.tools.Tools;

public class GetRoutes extends AsyncFather
{
    private WeakReference<SwipeRefreshLayout> refreshLayoutWeakReference;

    public GetRoutes(Context context, SwipeRefreshLayout refreshLayout, AsyncResponse response)
    {
        super(context, response);
        refreshLayoutWeakReference = new WeakReference<>(refreshLayout);
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        refreshLayoutWeakReference.get().setRefreshing(true);
    }

    @Override
    protected ArrayList<?> doInBackground(Object... objects)
    {
        ArrayList<DeliveryNote> deliveryNotes = null;

        try
        {
            if(super.isNetworkAvailable())
            {
                // 1. Getting all the routes for this deliveryman.
                Set<Route> routes = new RouteDAO().findByDeliveryman(Tools.getUser(appContext.get()).getId());

                // 2. For each route getting his delivery note.
                DeliveryNoteDAO deliveryNotesDB = new DeliveryNoteDAO();
                deliveryNotes = new ArrayList<>();
                DeliveryNote note;
                for(Route r : routes)
                {
                    for(int n : r.getDeliveryNotesIds())
                    {
                        note = deliveryNotesDB.findByPk(n);
                        if(note!=null)
                            deliveryNotes.add(note);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return deliveryNotes;
    }

    @Override
    protected void onCancelled()
    {
        super.onCancelled();
        refreshLayoutWeakReference.get().setRefreshing(false);
    }

    @Override
    protected void onPostExecute(ArrayList<?> objects)
    {
        super.onPostExecute(objects);
        refreshLayoutWeakReference.get().setRefreshing(false);
    }
}