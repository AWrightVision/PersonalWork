package edu.gatech.seclass.gobowl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.provider.Settings;

import java.util.ArrayList;

import edu.gatech.seclass.services.ServicesUtils;


/**
 * singleton
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // - singleton
    private static volatile DatabaseHelper instance;

    // - if you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 12;

    public static final String DATABASE_NAME = "gobowl.db";

    // - sql helper strings
    private static final String INT_TYPE = " INTEGER";
    private static final String TEXT_TYPE = " TEXT";

    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String NOT_NULL = " NOT NULL";
    private static final String COMMA_SEP = ", ";

    // - inner class for each table that enumerates the columns

    /**
     * CustomerTable provides all details on the customers in the system, this table is added to
     * and deleted from by the manager, and queries throughout the application
     */
    public static abstract class CustomerTable implements BaseColumns {
        public static final String TABLE_NAME = "customer";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_FIRST_NAME = "first_name";
        public static final String COLUMN_NAME_LAST_NAME = "last_name";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_TOTAL = "total";
        public static final String COLUMN_NAME_VIP_STATUS = "vip_status";

        public static final int DEFAULT_VALUE_TOTAL = 0;
        public static final int DEFAULT_VALUE_VIP_STATUS = 0;
        public static final int VIP_STATUS_TRUE = 1;
        public static final int VIP_STATUS_FALSE = DEFAULT_VALUE_VIP_STATUS;

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_NAME_ID + INT_TYPE + PRIMARY_KEY + COMMA_SEP +
            COLUMN_NAME_FIRST_NAME + TEXT_TYPE + COMMA_SEP +
            COLUMN_NAME_LAST_NAME + TEXT_TYPE + COMMA_SEP +
            COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
            COLUMN_NAME_TOTAL + INT_TYPE + COMMA_SEP +
            COLUMN_NAME_VIP_STATUS + INT_TYPE +  " )";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String SQL_SELECT_BY_EMAIL = "SELECT * FROM " + CustomerTable.TABLE_NAME +
            " WHERE " + COLUMN_NAME_EMAIL + " = ?";

        public static final String SQL_SELECT_BY_ID = "SELECT * FROM " + CustomerTable.TABLE_NAME +
            " WHERE " + COLUMN_NAME_ID + " = ?";

        public static final String SQL_SELECT_VIP_CUSTOMERS = "SELECT * FROM " + CustomerTable.TABLE_NAME +
            " WHERE " + CustomerTable.COLUMN_NAME_VIP_STATUS + " = " + CustomerTable.VIP_STATUS_TRUE;
        public static final String SQL_SELECT_VIP_COUNT = "SELECT count(*) FROM " + CustomerTable.TABLE_NAME +
            " WHERE " + CustomerTable.COLUMN_NAME_VIP_STATUS + " = " + CustomerTable.VIP_STATUS_TRUE;


        // - return status for table operations
        //   http://stackoverflow.com/questions/446663/best-way-to-define-error-codes-strings-in-java
        public enum Status {
            SUCCESS(1, "success"),
            INSERT_FAILED(-1, "Failed to insert into customer data"),
            DUPLICATE_EMAIL(-2, "This email address already exists");

            private final int code;
            private final String message;

            Status(int code, String message) {
                this.code = code;
                this.message = message;
            }

            public String getMessage() {
                return message;
            }

            public int getCode() {
                return code;
            }
        }
    }

    // Nicholas
    // Table associates a Lane to a Customer. Each Lane can have 1 customer.
    public static abstract class LaneCustomerTable implements BaseColumns {
        public static final String TABLE_NAME = "lane_customer";

        public static final String COLUMN_LANE_NUM = "lane_num";
        public static final String COLUMN_CUSTOMER_ID = "customer_id";
        public static final String COLUMN_LANE_REQUEST_HOUR = "lane_request_hour";
        public static final String COLUMN_LANE_REQUEST_MINITE = "lane_request_minite";
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_LANE_NUM + INT_TYPE + PRIMARY_KEY + COMMA_SEP +
                COLUMN_CUSTOMER_ID + INT_TYPE + COMMA_SEP +
                COLUMN_LANE_REQUEST_HOUR + INT_TYPE + COMMA_SEP +
                COLUMN_LANE_REQUEST_MINITE + INT_TYPE +" )";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    // Nicholas
    // Inner class for each table that enumerates the columns
    // Table associates a Player to a Lane. Each Player can play in 1 lane.
    public static abstract class PlayerLaneTable implements BaseColumns {
        public static final String TABLE_NAME = "player_lane";

        public static final String COLUMN_CUSTOMER_ID = "customer_id";
        public static final String COLUMN_LANE_NUM = "lane_num";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_CUSTOMER_ID + INT_TYPE  + COMMA_SEP +
                COLUMN_LANE_NUM + INT_TYPE + " )";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    // Create the table for store the lane prices
    public static abstract class LanePriceTable implements BaseColumns {
        public static final String TABLE_NAME = "lane_price";

        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_START_HOUR = "start_hour";
        public static final String COLUMN_END_HOUR = "end_hour";
        public static final String COLUMN_RATE_PER_HOUR = "rate_per_hour";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_DAY + INT_TYPE  + COMMA_SEP +
                COLUMN_START_HOUR + INT_TYPE  + COMMA_SEP +
                COLUMN_END_HOUR + INT_TYPE  + COMMA_SEP +
                COLUMN_RATE_PER_HOUR + INT_TYPE + " )";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //Nicholas
    // Create the table to store the score and time pairs
    public static abstract class ScoresTable implements BaseColumns {
        public static final String TABLE_NAME = "scores";

        public static final String COLUMN_TIME_STAMP = "time_stamp";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_CUSTOMER_ID = "customer_id";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_TIME_STAMP + INT_TYPE  + COMMA_SEP +
                COLUMN_SCORE + INT_TYPE  + COMMA_SEP +
                COLUMN_CUSTOMER_ID + INT_TYPE  +  " )";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    // - private for singleton
    private DatabaseHelper(Context context) {
        // - the database is created when this constructor is called
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * create an instance of the DatabaseHelper singleton
     *
     * http://stackoverflow.com/questions/70689/what-is-an-efficient-way-to-implement-a-singleton-pattern-in-java
     * http://stackoverflow.com/questions/7277087/database-global-instance
     * http://www.javaworld.com/article/2073352/core-java/simply-singleton.html
     * > is the inner condition variable safe? http://www.javaworld.com/article/2073352/core-java/simply-singleton.html
     */
    public static DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    // - create new db instance
                    instance = new DatabaseHelper(context.getApplicationContext());
                }
            }
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // - create the tables
        db.execSQL(CustomerTable.SQL_CREATE_TABLE);
        db.execSQL(LaneCustomerTable.SQL_CREATE_TABLE);
        db.execSQL(PlayerLaneTable.SQL_CREATE_TABLE);
        db.execSQL(LanePriceTable.SQL_CREATE_TABLE);
        db.execSQL(ScoresTable.SQL_CREATE_TABLE);
        insertLanePriceTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // - acts more like a cache?
        db.execSQL(CustomerTable.SQL_DROP_TABLE);
        db.execSQL(LaneCustomerTable.SQL_DROP_TABLE);
        db.execSQL(PlayerLaneTable.SQL_DROP_TABLE);
        db.execSQL(LanePriceTable.SQL_DROP_TABLE);
        db.execSQL(ScoresTable.SQL_DROP_TABLE);
        onCreate(db);

    }


    /***********************************************************************************************
     * CUSTOMER TABLE
     **********************************************************************************************/

    /**
     * insert into customer table
     *
     * @param firstName customer first name
     * @param lastName customer last name
     * @param email customer email address
     * @return custom status code
     */
    public CustomerTable.Status insertCustomerData(String firstName, String lastName, String email) {
        // - create instance of writable db class
        SQLiteDatabase db = this.getWritableDatabase();

        // - confirm their is space in the database
        //   because customer IDs are 4 digit hex codes the max entries in the table
        //   are 'ffff' == 15*(16**3) + 15*(16**2) + 15*(16**1) + 15*(16**0) == 2**16-1 = 65535
        //   this check should be performed on the largest primary key, and NOT the total rows

        // - confirm email is distinct (ie it doesnt already exist)
        Cursor cursor = db.rawQuery(CustomerTable.SQL_SELECT_BY_EMAIL, new String[] { email });
        if (cursor.getCount() > 0) {
            return CustomerTable.Status.DUPLICATE_EMAIL;
        }

        ContentValues contentValues = new ContentValues();
        // - integer primary key will be filled automatically (dont need autoincrement)
        //   https://www.sqlite.org/autoinc.html
        contentValues.put(CustomerTable.COLUMN_NAME_FIRST_NAME, firstName);
        contentValues.put(CustomerTable.COLUMN_NAME_LAST_NAME, lastName);
        contentValues.put(CustomerTable.COLUMN_NAME_EMAIL, email);
        contentValues.put(CustomerTable.COLUMN_NAME_TOTAL, CustomerTable.DEFAULT_VALUE_TOTAL);
        contentValues.put(CustomerTable.COLUMN_NAME_VIP_STATUS, CustomerTable.DEFAULT_VALUE_VIP_STATUS);

        long result = db.insert(CustomerTable.TABLE_NAME, null, contentValues);
        if (result == -1) {
            return CustomerTable.Status.INSERT_FAILED;
        } else {
            return CustomerTable.Status.SUCCESS;
        }
    }

    /**
     * get the total number of entries in the customer table
     *
     * @return number of rows in the customer table
     */
    public int getCustomerTableRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, CustomerTable.TABLE_NAME);
    }

    /**
     *
     * @return
     */
    public ArrayList<Customer> getAllCustomerData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CustomerTable.TABLE_NAME, null);

        ArrayList<Customer> customers = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                customers.add(createCustomerFromCursor(cursor));
            }
        }

        return customers;
    }

    /**
     * retrieve customer from database by email address
     *
     * @param email email address to query customer data on
     * @return if no data is found the method returns null else return newly constructed customer
     * object
     */
    public Customer getCustomerByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        // - execute query
        Cursor cursor = db.rawQuery(CustomerTable.SQL_SELECT_BY_EMAIL, new String[] { email });


        // - create customer
        Customer customer = null;
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            customer = createCustomerFromCursor(cursor);
        }

        // - return new customer
        return customer;
    }

    /**
     * retrieve customer from the database by ID
     *
     * @param id customer ID
     * @return
     */
    public Customer getCustomerById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // - execute query
        Cursor cursor = db.rawQuery(CustomerTable.SQL_SELECT_BY_ID, new String[] { id });


        // - create customer
        Customer customer = null;
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            customer = createCustomerFromCursor(cursor);
        }

        // - return new customer
        return customer;
    }

    /**
     * retrieve count of all VIP customers in the database
     *
     * @return number of VIP customers
     */
    public int getVipCustomerCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(CustomerTable.SQL_SELECT_VIP_COUNT, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    // Nicholas
    public String getCustomerNameById(Integer id ) {

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * " +
                     " FROM " + CustomerTable.TABLE_NAME +
                     " WHERE " + CustomerTable.COLUMN_NAME_ID + " == ?" ;
        Cursor cur = db.rawQuery(sql, new String[] { id.toString() });
        if (cur.getCount() == 1){
            cur.moveToFirst();
            return cur.getString(cur.getColumnIndex(CustomerTable.COLUMN_NAME_FIRST_NAME)) + " " + cur.getString(cur.getColumnIndex(CustomerTable.COLUMN_NAME_LAST_NAME));
        }
        return "";
    }

    public ArrayList<Customer> getVipCustomers() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(CustomerTable.SQL_SELECT_VIP_CUSTOMERS, null);

        ArrayList<Customer> customers = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                customers.add(createCustomerFromCursor(cursor));
            }
        }

        return customers;
    }

    /**
     * create a new customer from a cursor
     * calling procedure is responsible for checking that cursor is not-empty
     * and setting its location
     *
     * @param cursor database cursor which is already set to given item
     * @return
     */
    private Customer createCustomerFromCursor(Cursor cursor) {
        return new Customer(
            cursor.getInt(cursor.getColumnIndex(CustomerTable.COLUMN_NAME_ID)),
            cursor.getString(cursor.getColumnIndex(CustomerTable.COLUMN_NAME_FIRST_NAME)),
            cursor.getString(cursor.getColumnIndex(CustomerTable.COLUMN_NAME_LAST_NAME)),
            cursor.getString(cursor.getColumnIndex(CustomerTable.COLUMN_NAME_EMAIL)),
            cursor.getInt(cursor.getColumnIndex(CustomerTable.COLUMN_NAME_TOTAL)),
            cursor.getInt(cursor.getColumnIndex(CustomerTable.COLUMN_NAME_VIP_STATUS))
        );
    }

    /**
     *
     * @param id the database ID (not the corresponding hex ID for customer)
     * @param firstName
     * @param lastName
     * @param email
     * @param total
     * @param vip
     * @return
     */
    public boolean updateCustomerData(String id, String firstName, String lastName, String email, int total, int vip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CustomerTable.COLUMN_NAME_ID, id);
        contentValues.put(CustomerTable.COLUMN_NAME_FIRST_NAME, firstName);
        contentValues.put(CustomerTable.COLUMN_NAME_LAST_NAME, lastName);
        contentValues.put(CustomerTable.COLUMN_NAME_EMAIL, email);
        contentValues.put(CustomerTable.COLUMN_NAME_TOTAL, total);
        contentValues.put(CustomerTable.COLUMN_NAME_VIP_STATUS, vip);

        db.update(CustomerTable.TABLE_NAME, contentValues, CustomerTable.COLUMN_NAME_ID + " = ?", new String[] { id });
        return true;
    }

    public Integer deleteCustomerRecord(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CustomerTable.TABLE_NAME, CustomerTable.COLUMN_NAME_ID + " = ?", new String[] { id });
    }

    public Integer deleteAllCustomerTableRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CustomerTable.TABLE_NAME, null, null);
    }

    /**********************************************************************************************/

    public Integer deleteAllDatabaseRecords() {
        Integer rowsAffected = 0;
        rowsAffected += deleteAllCustomerTableRecords();
        rowsAffected += deleteAllLanesCustomers();
        rowsAffected += deleteAllLanesPlayers();
        rowsAffected += deleteAllScores();
        return rowsAffected;
    }

    public Integer deleteAllCustomerRecordsAcrossTables(String id) {
        Integer rowsAffects = 0;
        rowsAffects += deleteCustomerRecord(id);
        rowsAffects += deleteLaneCustomerTableRecordsById(id);
        rowsAffects += deletePlayerLaneTableRecordsById(id);
        rowsAffects += deleteScoresTableRecordsById(id);
        return rowsAffects;
    }

    public Integer deleteLaneCustomerTableRecordsById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(LaneCustomerTable.TABLE_NAME, LaneCustomerTable.COLUMN_CUSTOMER_ID + " = ?", new String[] { id });
    }

    public Integer deletePlayerLaneTableRecordsById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PlayerLaneTable.TABLE_NAME, PlayerLaneTable.COLUMN_CUSTOMER_ID + " = ?", new String[] { id });
    }

    public Integer deleteScoresTableRecordsById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ScoresTable.TABLE_NAME, ScoresTable.COLUMN_CUSTOMER_ID + " = ?", new String[] { id });
    }

    public Integer deleteAllLanesCustomers() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(LaneCustomerTable.TABLE_NAME, null, null);
    }

    public Integer deleteAllLanesPlayers() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PlayerLaneTable.TABLE_NAME, null, null);
    }

    public Integer deleteAllScores() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ScoresTable.TABLE_NAME, null, null);
    }



    public boolean insertScore(int score, int customer_id) {
        // - create instance of db class
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ScoresTable.COLUMN_TIME_STAMP, System.currentTimeMillis());
        contentValues.put(ScoresTable.COLUMN_SCORE, score);
        contentValues.put(ScoresTable.COLUMN_CUSTOMER_ID, customer_id);

        long result = db.insert(ScoresTable.TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //jake
    public boolean insertLaneCustomerData(int customerId, int hr, int minut, int ln) {
        // - create instance of db class
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(LaneCustomerTable.COLUMN_LANE_NUM, ln);
        contentValues.put(LaneCustomerTable.COLUMN_CUSTOMER_ID, customerId);
        contentValues.put(LaneCustomerTable.COLUMN_LANE_REQUEST_HOUR, hr);
        contentValues.put(LaneCustomerTable.COLUMN_LANE_REQUEST_MINITE, minut);

        long result = db.insert(LaneCustomerTable.TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //Nicholes
    // insert rate table
    //$20/hour from 9am to 5pm, Mon, Tue, Thu, Fri.
    //$25/hour from 5pm to midnight, Mon, Tue, Thu, Fri.
    //        $30/hour all day on Sat and Sun.
    //        $10/hour all day on Wed.
    public boolean insertLanePriceTable(SQLiteDatabase db) {


        int [][] rateTbl = {
                {2, 9, 17, 20},
                {3, 9, 17, 20},
                {5, 9, 17, 20},
                {6, 9, 17, 20},
                {2, 17, 24, 25},
                {3, 17, 24, 25},
                {5, 17, 24, 25},
                {6, 17, 24, 25},
                {4, 0, 24, 10},
                {1, 0, 24, 30},
                {7, 0, 24, 30},
        };
        for(int[] r: rateTbl){
            ContentValues contentValues = new ContentValues();

            contentValues.put(LanePriceTable.COLUMN_DAY, r[0]);
            contentValues.put(LanePriceTable.COLUMN_START_HOUR, r[1]);
            contentValues.put(LanePriceTable.COLUMN_END_HOUR, r[2]);
            contentValues.put(LanePriceTable.COLUMN_RATE_PER_HOUR, r[3]);

            long result = db.insert(LanePriceTable.TABLE_NAME, null, contentValues);
            if (result == -1) {
                return false;
            }
        }
        return true;
    }

    public boolean insertPlayerLaneData(int laneNum, int customerId) {
        // - create instance of db class
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(PlayerLaneTable.COLUMN_CUSTOMER_ID, customerId);
        contentValues.put(PlayerLaneTable.COLUMN_LANE_NUM, laneNum);

        long result = db.insert(PlayerLaneTable.TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Nicholas
    public ArrayList<Integer> getLanesByCustomer(int id ) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + LaneCustomerTable.TABLE_NAME + " Where customer_id == " + id + " ;", null);
        cur.moveToFirst();
        while (!cur.isAfterLast()){
            result.add(cur.getInt(cur.getColumnIndex(LaneCustomerTable.COLUMN_LANE_NUM)));
            cur.moveToNext();
        }
        return result;
    }

    // Nicholas
    public ArrayList<Integer> getLanesByCustomer(String email ) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * " +
            " FROM " + CustomerTable.TABLE_NAME + " AS ct " +
            " INNER JOIN " + LaneCustomerTable.TABLE_NAME + " AS lct " +
            " ON ct." + CustomerTable.COLUMN_NAME_ID + " == lct." + LaneCustomerTable.COLUMN_CUSTOMER_ID +
            " WHERE ct." + CustomerTable.COLUMN_NAME_EMAIL + " == ?";
        Cursor cur = db.rawQuery(sql, new String[] { email });

        cur.moveToFirst();
        while (!cur.isAfterLast()){
            result.add(cur.getInt(cur.getColumnIndex(LaneCustomerTable.COLUMN_LANE_NUM)));
            cur.moveToNext();
        }

        return result;
    }

    // Nicholas
    public int getVipByCustomer(String email ) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * " +
                " FROM " + CustomerTable.TABLE_NAME +
                " WHERE " + CustomerTable.COLUMN_NAME_EMAIL + " == \"" + email + "\" ;" , null);
        if (cur.getCount() == 1){
            cur.moveToFirst();
            return cur.getInt(cur.getColumnIndex(CustomerTable.COLUMN_NAME_VIP_STATUS));
        }
        return -1;
    }

    //Nicholas
    public ArrayList<Integer> getPlayersByLane(int ln ) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * " +
                " FROM " + PlayerLaneTable.TABLE_NAME +
                 " WHERE " + PlayerLaneTable.COLUMN_LANE_NUM + " == " + ln + " ;" , null);
        cur.moveToFirst();
        while (!cur.isAfterLast()){
            result.add(cur.getInt(cur.getColumnIndex(PlayerLaneTable.COLUMN_CUSTOMER_ID)));
            cur.moveToNext();
        }
        return result;
    }

    //Nicholas
    // if the query is inconclusive then it returns -1
    // inconclusive means that there is more than one rate or not reate for the day and hour
    public int getRate(int d, int h ) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * " +
                " FROM " + LanePriceTable.TABLE_NAME +
                " WHERE " + LanePriceTable.COLUMN_DAY + " == " + d + " AND " +
                         LanePriceTable.COLUMN_START_HOUR + " <= " + h + " AND " +
                               LanePriceTable.COLUMN_END_HOUR + " >= " + h + " ;" , null);

        if (cur.getCount() == 1){
            cur.moveToFirst();
            return cur.getInt(cur.getColumnIndex(LanePriceTable.COLUMN_RATE_PER_HOUR));
        }
        return -1;
    }

    //Nicholas
    public int getRequestHourByLane(int ln, int cust) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * " +
                " FROM " + LaneCustomerTable.TABLE_NAME +
                 " WHERE " + LaneCustomerTable.COLUMN_LANE_NUM + " == " + ln +
                " AND " +LaneCustomerTable.COLUMN_CUSTOMER_ID + " == " + cust + " ;" , null);
        if (cur.getCount() == 1){
            cur.moveToFirst();
            return cur.getInt(cur.getColumnIndex(LaneCustomerTable.COLUMN_LANE_REQUEST_HOUR));
        }
        return -1;
    }
    //Nicholas
    public int getRequestMiniteByLane(int ln, int cust) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * " +
                " FROM " + LaneCustomerTable.TABLE_NAME +
                " WHERE " + LaneCustomerTable.COLUMN_LANE_NUM + " == " + ln +
                " AND " +LaneCustomerTable.COLUMN_CUSTOMER_ID + " == " + cust + " ;" , null);
        if (cur.getCount() == 1){
            cur.moveToFirst();
            return cur.getInt(cur.getColumnIndex(LaneCustomerTable.COLUMN_LANE_REQUEST_MINITE));
        }
        return -1;
    }
    // jake
    public int getPlayerLaneFromId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT "+ PlayerLaneTable.COLUMN_LANE_NUM +
                " FROM " + PlayerLaneTable.TABLE_NAME + " AS pl " +
                " WHERE pl." + PlayerLaneTable.COLUMN_CUSTOMER_ID + " == " + String.valueOf(id) + " ;" , null);
        cur.moveToFirst();
        if (cur.getCount() == 0) return -1;

        else return cur.getInt(cur.getColumnIndex(PlayerLaneTable.COLUMN_LANE_NUM));

    }

    //jake
    public int getPlayerLaneTotalRows() {
        // - Gus' Note: this is not the right way to get all the rows in the table, also if you
        //   need to do it this way why dont you just return cur.getCount()? why moveToFirst()?
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cur = db.rawQuery("SELECT * FROM " + PlayerLaneTable.TABLE_NAME, null);
//        cur.moveToFirst();
//        return cur.getCount();
        SQLiteDatabase db = this.getWritableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, PlayerLaneTable.TABLE_NAME);
    }
}
