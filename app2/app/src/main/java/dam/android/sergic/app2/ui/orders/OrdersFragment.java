package dam.android.sergic.app2.ui.orders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;
import java.util.Objects;
import dam.android.sergic.app2.R;
import dam.android.sergic.app2.asyncTasks.AsyncFather;
import dam.android.sergic.app2.asyncTasks.UpdateStatus;
import dam.android.sergic.app2.db.BatoiLogicDBManager;
import dam.android.sergic.app2.models.Order;
import dam.android.sergic.app2.tools.Tools;

public class OrdersFragment extends Fragment implements MyAdapter.OnMyListeners, BatoiLogicDBManager.OnSyncCompleted, DialogInterface.OnClickListener, AsyncFather.AsyncResponse
{
    private View root;
    private RecyclerView recyclerView;
    private ImageView ivBackground;
    private SwipeRefreshLayout refreshLayout;

    private Order orderBack;

    private static final int REQUEST_STATUS = 0;
    private static final int OK = -1;
    private static final int PHONE = 0;
    private static final int EMAIL = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_order, container, false);
        setUI();

        if (getArguments() != null && getArguments().getBoolean("refresh", false))
        {
            if (recyclerView.getAdapter() != null)
                ((MyAdapter) recyclerView.getAdapter()).sync(refreshLayout);
        }

        return root;
    }

    private void setUI()
    {
        recyclerView = root.findViewById(R.id.rvOrders);
        ivBackground = root.findViewById(R.id.ivBackground);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager;
        if(isTablet())
        {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                layoutManager = new GridLayoutManager(getContext(),2);
            else
                layoutManager = new GridLayoutManager(getContext(),4);
        }
        else
        {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL, false);
            else
                layoutManager = new GridLayoutManager(getContext(),2);
        }
        recyclerView.setLayoutManager(layoutManager);

        MyAdapter mAdapter = new MyAdapter(getContext(), this);
        recyclerView.setAdapter(mAdapter);

        if(!mAdapter.isEmpty())
            ivBackground.setVisibility(View.GONE);

        setOnRefresh();
    }

    private boolean isTablet()
    {
        boolean tablet = false;

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=7.5)
        {
            tablet = true;
        }

        return tablet;
    }

    private void setOnRefresh()
    {
        refreshLayout = root.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(() ->
        {
            // Refresh items.
            if(recyclerView.getAdapter()!=null)
                ((MyAdapter) recyclerView.getAdapter()).sync(refreshLayout);
        });
    }

    @Override
    public synchronized void syncCompleted(boolean internet)
    {
        if(internet)
        {
            if(recyclerView.getAdapter()!=null)
            {
                ((MyAdapter)recyclerView.getAdapter()).getData();

                if(((MyAdapter)recyclerView.getAdapter()).isEmpty())
                    ivBackground.setVisibility(View.VISIBLE);
                else
                    ivBackground.setVisibility(View.GONE);
            }
        }
        else
            Toast.makeText(getContext(),getString(R.string.no_internet_available),Toast.LENGTH_SHORT).show();

        // Stop Animation.
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(Order item)
    {
        Intent intent = new Intent(getContext(), OrderDetailActivity.class);
        intent.putExtra("order", item);
        startActivityForResult(intent, REQUEST_STATUS);
    }

    @Override
    public boolean onItemLongClick(Order order)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));

        builder.setTitle(getString(R.string.select_the_action));
        builder.setItems(getResources().getStringArray(R.array.actions), (dialog, item) ->
        {
            if(item == PHONE)
            {
                // Opening the dialer with the user phone if there is any number associated.
                String tel = order.getCustomer().getPhone()+"";
                if(tel.length()>0 && !tel.equals("0"))
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+tel));
                    startActivity(intent);
                }
                else
                    Toast.makeText(getContext(),
                            getString(R.string.no_phone_available),Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(item == EMAIL)
                {
                    String email = order.getCustomer().getEmail().trim();

                    if(email.length()>0)
                    {
                        /* Create the Intent */
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);

                        /* Fill it with Data */
                        emailIntent.setType("plain/text");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_your_order));

                        /* Send it off to the Activity-Chooser */
                        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_mail)));
                    }
                }
            }
        });
        builder.show();
        return true;
    }

    /**
     * Method called when there is a change with the database.
     * @param internet --> Operation result
     */
    @Override
    public void onChangeDetected(boolean internet)
    {
        if(!internet)
            Tools.showAlert(getContext(),getString(R.string.error),getString(R.string.no_internet));
        else
        {
            if(recyclerView.getAdapter()!=null && ((MyAdapter)recyclerView.getAdapter()).isEmpty())
                ivBackground.setVisibility(View.VISIBLE);
            else
                ivBackground.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_STATUS && resultCode == Activity.RESULT_OK)
        {
            if (data != null)
            {
                orderBack = (Order) data.getSerializableExtra("orderBack");
                if(orderBack!=null && orderBack.getStatus()!=Order.OUT_FOR_DELIVERY)
                {
                    //revisar si funciona
                    Tools.showConfirmation(getContext(),getString(R.string.confirmation),getString(R.string.this_action, getResources().getStringArray(R.array.status)[orderBack.getStatus()-2]), this);
                }
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        if(which == OK)
        {
            refreshLayout.setRefreshing(true);
            UpdateStatus task = new UpdateStatus(getContext(), this, orderBack);
            task.execute();
        }
    }

    @Override
    public void processFinish(ArrayList<?> output)
    {
        refreshLayout.setRefreshing(false);

        if(output!=null && output.size()>0)
        {
            if(recyclerView.getAdapter()!=null)
                ((MyAdapter)recyclerView.getAdapter()).delete(output.get(0).toString().trim());

            if(recyclerView.getAdapter()!=null)
            {
                ((MyAdapter)recyclerView.getAdapter()).getData();

                if(((MyAdapter)recyclerView.getAdapter()).isEmpty())
                    ivBackground.setVisibility(View.VISIBLE);
                else
                    ivBackground.setVisibility(View.GONE);
            }
        }
        else
            Toast.makeText(getContext(),getString(R.string.impossible_to_update),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(recyclerView.getAdapter()!=null)
            ((MyAdapter)recyclerView.getAdapter()).cancelSync();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // Fixing bad image displaying when night mode is active.
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
        {
            case Configuration.UI_MODE_NIGHT_YES:
                ivBackground.setColorFilter(Color.WHITE);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                ivBackground.setColorFilter(Color.BLACK);
                break;
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        // close any connection to DB
        if(recyclerView!=null && recyclerView.getAdapter()!=null)
            ((MyAdapter)recyclerView.getAdapter()).closeDb();
    }
}