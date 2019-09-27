package com.platform.myapp.myapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class myDBClass extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "mydatabase";

    // Table Name
    public static final String TABLE_CONTACTS = "contacts";

    public myDBClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // Create Table Name
        db.execSQL("CREATE TABLE " + TABLE_CONTACTS +
                "(ContactID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " Name VARCHAR(100)," +
                " Email VARCHAR(100)," +
                " Tel VARCHAR(10));");


        Log.d("CREATE TABLE","Create Table Successfully.");
    }

    // Insert Data
    public long InsertData(String strContactID, String strName, String strEmail, String strTel) {
        // TODO Auto-generated method stub

        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put("ContactID", strContactID);
            Val.put("Name", strName);
            Val.put("Email", strEmail);
            Val.put("Tel", strTel);

            long rows = db.insert(TABLE_CONTACTS, null, Val);

            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }

    }


    // Update Data
    public long UpdateData(String strContactID,String strName,String strEmail,String strTel) {
        // TODO Auto-generated method stub

        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put("Name", strName);
            Val.put("Email", strEmail);
            Val.put("Tel", strTel);

            long rows = db.update(TABLE_CONTACTS, Val, " ContactID = ?",
                    new String[] { String.valueOf(strContactID) });

            db.close();
            return rows; // return rows updated.

        } catch (Exception e) {
            return -1;
        }

    }


    // Delete Data
    public long DeleteData(String strContactID) {
        // TODO Auto-generated method stub

        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            long rows = db.delete(TABLE_CONTACTS, "ContactID = ?",
                    new String[] { String.valueOf(strContactID) });

            db.close();
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }

    }


    // Select Data
    public String[] SelectData(String strContactID) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            Cursor cursor = db.query(TABLE_CONTACTS, new String[] { "*" },
                    "ContactID=?",
                    new String[] { String.valueOf(strContactID) }, null, null, null, null);

            if(cursor != null)
            {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];
                    /***
                     *  0 = ContactID
                     *  1 = Name
                     *  2 = Email
                     *  3 = Tel
                     */
                    arrData[0] = cursor.getString(0);
                    arrData[1] = cursor.getString(1);
                    arrData[2] = cursor.getString(2);
                    arrData[3] = cursor.getString(3);
                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }



    // Show All Data
    public ArrayList<HashMap<String, String>> SelectAllData() {
        // TODO Auto-generated method stub

        try {

            ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT  * FROM " + TABLE_CONTACTS;
            Cursor cursor = db.rawQuery(strSQL, null);

            if(cursor != null)
            {
                if (cursor.moveToFirst()) {
                    do {
                        map = new HashMap<String, String>();
                        map.put("ContactID", cursor.getString(0));
                        map.put("Name", cursor.getString(1));
                        map.put("Email", cursor.getString(2));
                        map.put("Tel", cursor.getString(3));
                        MyArrList.add(map);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return MyArrList;

        } catch (Exception e) {
            return null;
        }

    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Re Create on method  onCreate
        onCreate(db);
    }

}