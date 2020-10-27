package dam.android.sergic.app2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BatoiLogicDBHelper extends SQLiteOpenHelper
{
    // instance to SQLiteOPenHelper
    private static BatoiLogicDBHelper instanceDBHelper;

    // This method assures only one instance of TodoListDBHelper for all the application.
    // Use the application context, to not leak Activity context
    public static synchronized BatoiLogicDBHelper getInstance(Context context)
    {
        // instance must be unique
        if(instanceDBHelper == null)
            instanceDBHelper = new BatoiLogicDBHelper(context.getApplicationContext());

        return instanceDBHelper;
    }

    // Constructor should be private to prevent direct instantiation.
    private BatoiLogicDBHelper(Context context)
    {
        super(context,BatoiLogicDBContract.DB_NAME,null,BatoiLogicDBContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(BatoiLogicDBContract.Address.CREATE_TABLE);
        db.execSQL(BatoiLogicDBContract.Customers.CREATE_TABLE);
        db.execSQL(BatoiLogicDBContract.Orders.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // the upgrade policy is simply discard the table and start over
        db.execSQL(BatoiLogicDBContract.Orders.DELETE_TABLE);
        db.execSQL(BatoiLogicDBContract.Customers.DELETE_TABLE);
        db.execSQL(BatoiLogicDBContract.Address.DELETE_TABLE);

        // create again the DB
        onCreate(db);
    }
}