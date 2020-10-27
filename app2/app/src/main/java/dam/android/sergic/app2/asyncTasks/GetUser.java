package dam.android.sergic.app2.asyncTasks;

import android.content.Context;
import android.widget.ProgressBar;
import java.util.ArrayList;
import dam.android.sergic.app2.dao.DeliverymanDAO;
import dam.android.sergic.app2.models.Deliveryman;

public class GetUser extends AsyncFather
{
    public GetUser(Context context, ProgressBar pb, AsyncResponse response)
    {
        super(context, pb, response);
    }

    /**
     * Method to retrieve in background information, in this case the user, if there is not internet
     * connection returns null, otherwise a list with the user or a exception.
     *
     * @param objects   --> The nickname (String)
     * @return          --> The user (inside a list).
     */
    @Override
    protected ArrayList<Deliveryman> doInBackground(Object... objects)
    {
        ArrayList<Deliveryman> userList = null;

        if(super.isNetworkAvailable())
        {
            userList = new ArrayList<>();

            try
            {
                userList.add(new DeliverymanDAO().findByNickname((String)objects[0]));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return userList;
    }
}