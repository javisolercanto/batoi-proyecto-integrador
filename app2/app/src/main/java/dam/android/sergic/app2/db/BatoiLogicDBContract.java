package dam.android.sergic.app2.db;

public class BatoiLogicDBContract
{
    // Database name
    public static final String DB_NAME = "BATOI_LOGIC.DB";
    // Database Version
    public static final int DB_VERSION = 5;

    // To prevent someone from accidentally instantiating the contract class:
    // make the constructor private;
    private BatoiLogicDBContract()
    {}

    // schema

    // TABLE Customers
    public static class Customers
    {
        // Table name
        public static final String TABLE_NAME = "CUSTOMERS";

        // Columns names
        public static final String _ID = "id";
        public static final String NAME = "name";
        public static final String PHONE = "phone";
        public static final String EMAIL = "email";

        // CREATE_TABLE SQL String
        public static final String CREATE_TABLE = "CREATE TABLE " + Customers.TABLE_NAME
                + "("
                + Customers._ID + " INTEGER PRIMARY KEY, "
                + Customers.NAME + " TEXT NOT NULL, "
                + Customers.PHONE + " INTEGER, "
                + Customers.EMAIL + " TEXT"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + Customers.TABLE_NAME;
        public  static final String DROP_ROWS = "DELETE FROM "+Customers.TABLE_NAME;

        // other table definition would come here
    }

    // TABLE Address: Inner class that defines the table tasks contents
    public static class Address
    {
        // Table name
        public static final String TABLE_NAME = "ADDRESS";

        // Columns names
        public static final String _ID = "id";
        public static final String STREET = "street";
        public static final String POSTAL_CODE = "zip";
        public static final String CITY = "city";
        public static final String PROVINCE = "province";

        // CREATE_TABLE SQL String
        public static final String CREATE_TABLE = "CREATE TABLE " + Address.TABLE_NAME
                + "("
                + Address._ID + " INTEGER PRIMARY KEY, "
                + Address.STREET + " TEXT, "
                + Address.POSTAL_CODE + " INTEGER, "
                + Address.CITY + " TEXT, "
                + Address.PROVINCE + " TEXT"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + Address.TABLE_NAME;
        public  static final String DROP_ROWS = "DELETE FROM "+Address.TABLE_NAME;

        // other table definition would come here
    }

    // TABLE ORDERS: Inner class that defines the table tasks contents
    public static class Orders
    {
        // Table name
        public static final String TABLE_NAME = "ORDERS";

        // Columns names
        public static final String _ID = "order_number";
        public static final String REQUEST_DATE = "request_date";
        public static final String STATUS = "status";
        public static final String ABOUT = "about";
        public static final String NOTES = "delivery_note_notes";
        public static final String CUSTOMER = "customer";
        public static final String ADDRESS = "address";

        // CREATE_TABLE SQL String
        public static final String CREATE_TABLE = "CREATE TABLE " + Orders.TABLE_NAME
                + "("
                + Orders._ID + " TEXT PRIMARY KEY, "
                + Orders.REQUEST_DATE + " TEXT, "
                + Orders.STATUS + " INTEGER, "
                + Orders.ABOUT + " TEXT, "
                + Orders.NOTES + " TEXT, "
                + Orders.CUSTOMER + " INTEGER, "
                + Orders.ADDRESS + " INTEGER, "
                + "FOREIGN KEY("+Orders.ADDRESS+") REFERENCES "+Address.TABLE_NAME+"("+Address._ID+"), "
                + "FOREIGN KEY("+Orders.CUSTOMER+") REFERENCES "+Customers.TABLE_NAME+"("+Customers._ID+")"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + Orders.TABLE_NAME;
        public  static final String DROP_ROWS = "DELETE FROM "+Orders.TABLE_NAME;

        // other table definition would come here
    }
}