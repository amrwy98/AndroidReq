package com.example.reminder;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class RemindersDbAdapter {

    //these are the column names
    public static final String COL_ID = "_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMPORTANT = "important";
    //these are the corresponding indices
    public static final int INDEX_ID = 0;
    public static final int INDEX_CONTENT = INDEX_ID + 1;
    public static final int INDEX_IMPORTANT = INDEX_ID + 2;
    //used for logging
    private static final String TAG = "RemindersDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "dba_remdrs.db";
    private static final String TABLE_NAME = "tbl_remdrs";
    private static final int DATABASE_VERSION = 1;
    private final Context mCtx;
    //SQL statement used to create the database
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_CONTENT + " TEXT, " +
                    COL_IMPORTANT + " INTEGER );";


    public RemindersDbAdapter(Context ctx) {
        Log.d("fel Db Adapter","Object created");
        this.mCtx = ctx;
    }
    //open
    public void open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        Log.d("fel open","DatabaseHelper created");
        mDb = mDbHelper.getWritableDatabase();
    }
    //close
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


    //TODO implement the function createReminder() which take the name as the content of the reminder and boolean important...note that the id will be created for you automatically//DONE
    public void createReminder(String name, boolean important) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CONTENT,name);
        contentValues.put(COL_IMPORTANT,important);
        mDb.insert(TABLE_NAME,null,contentValues);


    }
    //TODO overloaded to take a reminder//DONE
   public long createReminder(Reminder reminder) {
       ContentValues contentValues = new ContentValues();
       contentValues.put(COL_CONTENT,reminder.getContent());
       contentValues.put(COL_IMPORTANT,reminder.getImportant());
       return mDb.insert(TABLE_NAME,null,contentValues);
    }

    //TODO implement the function fetchReminderById() to get a certain reminder given its id//DONE
    public Reminder fetchReminderById(int id) {
        Cursor rems= mDb.rawQuery("select * from " + TABLE_NAME + " where "+COL_ID+" = " + String.valueOf(id), null);
        rems.moveToFirst();
        Reminder rem = new Reminder(id,rems.getString(INDEX_CONTENT),Integer.valueOf(rems.getString(INDEX_IMPORTANT)));
        return rem;
    }


    //TODO implement the function fetchAllReminders() which get all reminders//DONE
    public Cursor fetchAllReminders() {
        Cursor rems= mDb.rawQuery("select * from " + TABLE_NAME, null);
        return rems;
    }

    //TODO implement the function updateReminder() to update a certain reminder//DONE
    public void updateReminder(Reminder reminder) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID,reminder.getId());
        contentValues.put(COL_CONTENT,reminder.getContent());
        contentValues.put(COL_IMPORTANT,reminder.getImportant());
        mDb.update(TABLE_NAME,contentValues,"_id = ?", new String[] {String.valueOf(reminder.getId())});

    }
    //TODO implement the function deleteReminderById() to delete a certain reminder given its id//DONE
    public void deleteReminderById(int nId) {
        mDb.delete(TABLE_NAME,"_id = ?", new String[] {String.valueOf(nId)});
    }

    //TODO implement the function deleteAllReminders() to delete all reminders//DONE
    public void deleteAllReminders() {
        mDb.delete(TABLE_NAME,"1",null);
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }


}

