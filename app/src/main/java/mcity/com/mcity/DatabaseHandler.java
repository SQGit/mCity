package mcity.com.mcity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 08-11-2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "McityDataBase";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_STATION_NAME = "station_name";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_ADDRESS = "key_address";
    private static final String KEY_TIME = "time";
    private static final String KEY_PHONE1= "phone1";
    private static final String KEY_PHONE2= "phone2";
    private static final String KEY_PHONE3= "phone3";
    private static final String KEY_NAME1= "name1";
    private static final String KEY_NAME2= "name2";
    private static final String KEY_NAME3= "name3";




    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_STATION_NAME + " TEXT,"
                + KEY_LAT + " TEXT," + KEY_LNG + " TEXT," + KEY_ADDRESS + " TEXT,"
                +KEY_TIME + " TEXT," +KEY_PHONE1 + " TEXT," +KEY_PHONE2 + " TEXT,"
                +KEY_PHONE3 + " TEXT," +KEY_NAME1 + " TEXT," +KEY_NAME2 + " TEXT,"
                +KEY_NAME3 + " TEXT)";



        db.execSQL(CREATE_CONTACTS_TABLE);

        Log.e("TEST", "onCreate --------------- >>");
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }


    public void addContact(PlaceDetails contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATION_NAME, contact.getPlaceName());
        values.put(KEY_LAT, contact.getLatitude());
        values.put(KEY_LNG, contact.getLongitude());
        values.put(KEY_ADDRESS, contact.getAddress());
        values.put(KEY_TIME, contact.getTime());

        values.put(KEY_PHONE1, contact.getPhone1());
        values.put(KEY_PHONE2, contact.getPhone2());
        values.put(KEY_PHONE3, contact.getPhone3());
        values.put(KEY_NAME1, contact.getname1());
        values.put(KEY_NAME2, contact.getname2());
        values.put(KEY_NAME3, contact.getname3());

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }





    public List<PlaceDetails> getAllContacts() {
        List<PlaceDetails> contactList = new ArrayList<PlaceDetails>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int cnt = cursor.getCount();
        Log.d("TEST","onCreate cnt --------------- >>"  + cnt);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PlaceDetails contact = new PlaceDetails();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setPlaceName(cursor.getString(1));
                contact.setLatitude(cursor.getString(2));
                contact.setLongitude(cursor.getString(3));
                contact.setAddress(cursor.getString(4));
                contact.setTime(cursor.getString(5));

                contact.setPhone1(cursor.getString(6));
                contact.setPhone2(cursor.getString(7));
                contact.setPhone3(cursor.getString(8));

                contact.setname1(cursor.getString(9));
                contact.setname2(cursor.getString(10));
                contact.setname3(cursor.getString(11));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        Log.d("TEST","onCreate cnt --------------- >>"  + contactList);

        // return contact list
        return contactList;
    }
    //delete table name
    public void deleteTable( ) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS,null,null);
        db.close(); // Closing database connection
    }
}

