package dam.android.sergic.app2.ui.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.List;
import dam.android.sergic.app2.R;
import dam.android.sergic.app2.db.BatoiLogicDBManager;
import dam.android.sergic.app2.models.Order;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements BatoiLogicDBManager.OnSyncCompleted
{
    // ---------- Interface - Listener Pattern -------------------
    public interface OnMyListeners
    {
        void onItemClick(Order item);
        boolean onItemLongClick(Order item);
        void onChangeDetected(boolean internet);
    }
    // -----------------------------------------------------------

    private BatoiLogicDBManager managerDB;
    private List<Order> myDataSet;
    private OnMyListeners listener;

    static class MyViewHolder extends RecyclerView.ViewHolder
    {

        private TextView tvNumber;
        private TextView tvCustomer;
        private TextView tvAddress;

        private MyViewHolder(View view) {

            super(view);
            this.tvNumber = view.findViewById(R.id.tvNumber);
            this.tvCustomer = view.findViewById(R.id.tvCustomer);
            this.tvAddress = view.findViewById(R.id.tvAddress);
        }

        private void bind(final Order order, final OnMyListeners listener, final int position)
        {
            this.tvNumber.setText(String.valueOf(position+1));
            this.tvCustomer.setText(order.getCustomer().getName());
            this.tvAddress.setText(order.getAddress().getStreet());
            this.itemView.setOnClickListener(v -> listener.onItemClick(order));
            this.itemView.setOnLongClickListener(l -> listener.onItemLongClick(order));
        }
    }

    public MyAdapter(Context context, OnMyListeners listener)
    {
        this.managerDB = new BatoiLogicDBManager(context, this);
        this.listener = listener;

        getData();
    }

    // ---------------------- Operations. ------------------------------

    public void sync(SwipeRefreshLayout refreshLayout)
    {
        this.managerDB.sync(refreshLayout);
    }

    public void cancelSync()
    {
        this.managerDB.cancelSync();
    }

    @Override
    public void syncCompleted(boolean internet)
    {
        getData();
        listener.onChangeDetected(internet);
    }

    // get data from DB
    public void getData()
    {
        this.myDataSet = managerDB.getOrders();
        notifyDataSetChanged();
    }

    public void delete(String orderNumber)
    {
        managerDB.delete(orderNumber);
        getData();
    }

    public void closeDb()
    {
        this.managerDB.close();
    }

    public boolean isEmpty()
    {
        return myDataSet.isEmpty();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_recycler_item, parent,
                false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position)
    {
        viewHolder.bind(myDataSet.get(position), listener, position);
    }

    @Override
    public int getItemCount()
    {
        return myDataSet.size();
    }
}