package dam.android.sergic.app2.ui.orders;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import org.jetbrains.annotations.NotNull;
import dam.android.sergic.app2.R;
import dam.android.sergic.app2.models.Order;

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView tvHeader;
    private TextView tvStatus;
    private Spinner spinnerStatus;
    private EditText etObservations;

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        order = (Order) getIntent().getSerializableExtra("order");
        setUI();
    }

    private void setUI()
    {
        tvHeader = findViewById(R.id.tvHeader);
        TextView tvAddress = findViewById(R.id.tvAddress);
        tvStatus = findViewById(R.id.tvStatus);
        TextView tvNotes = findViewById(R.id.tvNotes);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        etObservations = findViewById(R.id.etObservations);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter
                .createFromResource(this, R.array.status, android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerStatus.setAdapter(spinnerAdapter);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (getResources().getStringArray(R.array.status)[position]
                        .equals(getResources().getStringArray(R.array.status)[2]))
                    etObservations.setVisibility(View.VISIBLE);
                else
                    etObservations.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        tvHeader.setText(order.getCustomer().getName());
        tvAddress.setText(order.getAddress().getStreet());
        etObservations.setText(getString(R.string.address_incorrect));                              // By default showing Address Incorrect!.

        if(order.getNotes().length()>0)
        {
            tvNotes.setText(order.getNotes());
            tvNotes.setVisibility(View.VISIBLE);
        }

        setTitle(order.getOrderNumber()+" | "+order.getDate());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                tvStatus.setTextColor(getResources().getColor(android.R.color.white));
                tvHeader.setTextColor(getResources().getColor(android.R.color.white));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                tvStatus.setTextColor(getColor(R.color.colorAccent));
                tvHeader.setTextColor(getColor(R.color.colorAccent));
                break;
        }
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.btnAccept)
        {
            order.setStatus(spinnerStatus.getSelectedItemPosition()+2);     // +2 Is because PENDING and READY.
            order.setAbout(etObservations.getText().toString());

            Intent result = new Intent();
            result.putExtra("orderBack",order);
            setResult(Activity.RESULT_OK, result);
        }

        finish();
    }

    // Saving UI State
    @Override
    public void onSaveInstanceState(@NotNull Bundle outState)
    {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
        outState.putString("text", etObservations.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        etObservations.setText(savedInstanceState.getString("text"));
    }
}