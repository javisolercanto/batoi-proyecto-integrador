package dam.android.sergic.app2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;
import dam.android.sergic.app2.asyncTasks.GetRoutes;
import dam.android.sergic.app2.asyncTasks.AsyncFather;
import dam.android.sergic.app2.models.Address;
import dam.android.sergic.app2.models.Customer;
import dam.android.sergic.app2.models.DeliveryNote;
import dam.android.sergic.app2.models.Order;

public class BatoiLogicDBManager implements AsyncFather.AsyncResponse
{
    private BatoiLogicDBHelper helper;
    private Context context;
    private OnSyncCompleted listener;
    private GetRoutes apiCall;

    // ------------- Interface --------------------------------
    public interface OnSyncCompleted
    {
        void syncCompleted(boolean internet);
    }
    // --------------------------------------------------------

    public BatoiLogicDBManager(Context context)
    {
        this.context = context;
        helper = BatoiLogicDBHelper.getInstance(context);
    }

    public BatoiLogicDBManager(Context context, OnSyncCompleted listener)
    {
        this.listener = listener;
        this.context = context;
        helper = BatoiLogicDBHelper.getInstance(context);
    }

    public void sync(SwipeRefreshLayout refreshLayout)
    {
        apiCall = new GetRoutes(context, refreshLayout, this);
        apiCall.execute();
    }

    public void cancelSync()
    {
        if(apiCall!=null && (apiCall.getStatus()== AsyncTask.Status.PENDING || apiCall.getStatus()== AsyncTask.Status.RUNNING))
        {
            apiCall.cancel(true);
        }
    }

    @Override
    public void processFinish(ArrayList<?> output)
    {
        boolean internet = false;

        if(output!=null)
        {
            internet = true;
            for(Object d : output)
            {
                Order order = ((DeliveryNote) d).getOrder();
                order.setNotes(((DeliveryNote) d).getNotes());

                insert(order.getAddress());
                insert(order.getCustomer());
                insert(order);
            }
        }

        if(listener!=null)
            listener.syncCompleted(internet);
    }

    // Operations ------------------------------------------------------------------------------

    // CREATE new row
    public void insert(Customer customer)
    {
        // open database to read and write
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();

        if(sqLiteDatabase != null)
        {
            ContentValues contentValue = new ContentValues();

            contentValue.put(BatoiLogicDBContract.Customers._ID, customer.getId());
            contentValue.put(BatoiLogicDBContract.Customers.NAME, customer.getName());
            contentValue.put(BatoiLogicDBContract.Customers.PHONE, customer.getPhone());
            contentValue.put(BatoiLogicDBContract.Customers.EMAIL, customer.getEmail());

            sqLiteDatabase.insertWithOnConflict(BatoiLogicDBContract.Customers.TABLE_NAME,null,contentValue, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    public void insert(Order order)
    {
        // open database to read and write
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();

        if(sqLiteDatabase != null)
        {
            ContentValues contentValue = new ContentValues();

            contentValue.put(BatoiLogicDBContract.Orders._ID, order.getOrderNumber());
            contentValue.put(BatoiLogicDBContract.Orders.REQUEST_DATE, order.getDate());
            contentValue.put(BatoiLogicDBContract.Orders.ABOUT, order.getAbout());
            contentValue.put(BatoiLogicDBContract.Orders.STATUS, order.getStatus());
            contentValue.put(BatoiLogicDBContract.Orders.NOTES, order.getNotes());
            contentValue.put(BatoiLogicDBContract.Orders.ADDRESS, order.getAddress().getId());
            contentValue.put(BatoiLogicDBContract.Orders.CUSTOMER, order.getCustomer().getId());

            sqLiteDatabase.insertWithOnConflict(BatoiLogicDBContract.Orders.TABLE_NAME,null,contentValue, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    public void insert(Address address)
    {
        // open database to read and write
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();

        if(sqLiteDatabase != null)
        {
            ContentValues contentValue = new ContentValues();

            contentValue.put(BatoiLogicDBContract.Address._ID, address.getId());
            contentValue.put(BatoiLogicDBContract.Address.STREET, address.getStreet());
            contentValue.put(BatoiLogicDBContract.Address.POSTAL_CODE, address.getZip());
            contentValue.put(BatoiLogicDBContract.Address.CITY, address.getCity());
            contentValue.put(BatoiLogicDBContract.Address.PROVINCE, address.getProvince());

            sqLiteDatabase.insertWithOnConflict(BatoiLogicDBContract.Address.TABLE_NAME,null,contentValue, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    // Get all data form Orders table
    public ArrayList<Order> getOrders()
    {
        ArrayList<Order> orders = new ArrayList<>();

        // open database to read
        SQLiteDatabase db = helper.getReadableDatabase();

        if(db != null)
        {
            String [] projection = new String[] {BatoiLogicDBContract.Orders._ID,
                    BatoiLogicDBContract.Orders.REQUEST_DATE,
                    BatoiLogicDBContract.Orders.STATUS,
                    BatoiLogicDBContract.Orders.ABOUT,
                    BatoiLogicDBContract.Orders.NOTES,
                    BatoiLogicDBContract.Orders.ADDRESS,
                    BatoiLogicDBContract.Orders.CUSTOMER};

            Cursor cursor = db.query(BatoiLogicDBContract.Orders.TABLE_NAME,
                    projection,                           // The columns toView return
                    null,                      // no WHERE clause
                    null,                  // no values for the WHERE clause
                    null,                       // don't group the rows
                    null,                        // don't filter by row groups
                    null);                      // don't sort rows

            if(cursor != null)
            {
                // get the column indexes for required columns
                int _idIndex = cursor.getColumnIndexOrThrow(BatoiLogicDBContract.Orders._ID);
                int dateIndex = cursor.getColumnIndexOrThrow(BatoiLogicDBContract.Orders.REQUEST_DATE);
                int statusIndex = cursor.getColumnIndexOrThrow(BatoiLogicDBContract.Orders.STATUS);
                int aboutIndex = cursor.getColumnIndexOrThrow(BatoiLogicDBContract.Orders.ABOUT);
                int notesIndex = cursor.getColumnIndexOrThrow(BatoiLogicDBContract.Orders.NOTES);
                int addressIndex = cursor.getColumnIndexOrThrow(BatoiLogicDBContract.Orders.ADDRESS);
                int customerIndex = cursor.getColumnIndexOrThrow(BatoiLogicDBContract.Orders.CUSTOMER);

                // read data and add to ArrayList
                while(cursor.moveToNext())
                {
                    Order order = new Order();

                    order.setOrderNumber(cursor.getString(_idIndex));
                    order.setDate(cursor.getString(dateIndex));                                 // SQLite there isn't any Date type.
                    order.setStatus(cursor.getInt(statusIndex));
                    order.setAbout(cursor.getString(aboutIndex));
                    order.setNotes(cursor.getString(notesIndex));
                    order.setAddress(findByPkAddress(cursor.getInt(addressIndex)));
                    order.setCustomer(findByPkCustomer(cursor.getInt(customerIndex)));

                    orders.add(order);
                }
                // close cursor to free resources
                cursor.close();
            }
        }
        return orders;
    }

    public void delete(String id)
    {
        // open database to read and write
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();

        if(sqLiteDatabase != null)
        {
            String deleteQuery = "DELETE FROM ORDERS where order_number ='"+ id +"'";
            sqLiteDatabase.execSQL(deleteQuery);
        }
    }

    private Customer findByPkCustomer(int id)
    {
        Customer customer = null;

        // open database to read
        SQLiteDatabase db = helper.getReadableDatabase();

        if(db != null)
        {
            String [] projection = new String[] {BatoiLogicDBContract.Customers._ID,
                    BatoiLogicDBContract.Customers.NAME,
                    BatoiLogicDBContract.Customers.EMAIL,
                    BatoiLogicDBContract.Customers.PHONE};

            Cursor cursor = db.query(BatoiLogicDBContract.Customers.TABLE_NAME,
                    projection,                           // The columns toView return
                    BatoiLogicDBContract.Customers._ID+"="+id,                      // no WHERE clause
                    null,                  // no values for the WHERE clause
                    null,                       // don't group the rows
                    null,                        // don't filter by row groups
                    null);                      // don't sort rows

            if(cursor != null)
            {
                // get the column indexes for required columns
                int _idIndex = cursor.getColumnIndexOrThrow(BatoiLogicDBContract.Customers._ID);
                int nameIndex = cursor.getColumnIndexOrThrow(BatoiLogicDBContract.Customers.NAME);
                int emailIndex = cursor.getColumnIndexOrThrow(BatoiLogicDBContract.Customers.EMAIL);
                int phoneIndex = cursor.getColumnIndexOrThrow(BatoiLogicDBContract.Customers.PHONE);

                // read data and add to ArrayList
                while(cursor.moveToNext())
                {
                    customer = new Customer();

                    customer.setId(cursor.getInt(_idIndex));
                    customer.setName(cursor.getString(nameIndex));
                    customer.setPhone(cursor.getInt(phoneIndex));
                    customer.setEmail(cursor.getString(emailIndex));

                }
                // close cursor to free resources
                cursor.close();
            }
        }
        return customer;
    }

    private Address findByPkAddress(int id)
    {
        Address address = null;

        // open database to read
        SQLiteDatabase db = helper.getReadableDatabase();

        if(db != null)
        {
            String [] projection = new String[]{BatoiLogicDBContract.Address.STREET,
                    BatoiLogicDBContract.Address.POSTAL_CODE,
                    BatoiLogicDBContract.Address.CITY,
                    BatoiLogicDBContract.Address.PROVINCE};

            Cursor cursor = db.query(BatoiLogicDBContract.Address.TABLE_NAME,
                    projection,                           // The columns toView return
                    BatoiLogicDBContract.Address._ID+"="+id,                      // no WHERE clause
                    null,                  // no values for the WHERE clause
                    null,                       // don't group the rows
                    null,                        // don't filter by row groups
                    null);                      // don't sort rows

            if(cursor != null)
            {
                // get the column indexes for required columns
                int streetIndex = cursor.getColumnIndexOrThrow(BatoiLogicDBContract.Address.STREET);
                int zipIndex = cursor.getColumnIndexOrThrow(BatoiLogicDBContract.Address.POSTAL_CODE);
                int cityIndex = cursor.getColumnIndexOrThrow(BatoiLogicDBContract.Address.CITY);
                int provinceIndex = cursor.getColumnIndexOrThrow(BatoiLogicDBContract.Address.PROVINCE);

                // read data and add to ArrayList
                while(cursor.moveToNext())
                {
                    address = new Address();

                    address.setStreet(cursor.getString(streetIndex));
                    address.setZip(cursor.getInt(zipIndex));
                    address.setCity(cursor.getString(cityIndex));
                    address.setProvince(cursor.getString(provinceIndex));
                }
                // close cursor to free resources
                cursor.close();
            }
        }
        return address;
    }

    public void drop(){
        // open database to read and write
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();

        if(sqLiteDatabase != null)
        {
            sqLiteDatabase.execSQL(BatoiLogicDBContract.Orders.DROP_ROWS);
            sqLiteDatabase.execSQL(BatoiLogicDBContract.Address.DROP_ROWS);
            sqLiteDatabase.execSQL(BatoiLogicDBContract.Customers.DROP_ROWS);
        }
    }
    public void close()
    {
        helper.close();           // closes any opened database object
        cancelSync();
    }
}