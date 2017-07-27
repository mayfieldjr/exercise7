package com.example.exercise7;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.exercise7.FeedReaderContract.FeedEntry;

public class MainActivity extends AppCompatActivity
{
    EditText title1;
    EditText subtitle1;
    EditText randomv;
    TextView showdb1;

    private static final String TAG = MainActivity.class.getSimpleName() + "_TAG";
    private DBHelper helper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new DBHelper(this);
        database = helper.getWritableDatabase();
        title1 = (EditText) findViewById(R.id.title);
        subtitle1 = (EditText) findViewById(R.id.subtitle);
        showdb1 = (TextView) findViewById(R.id.showdb);
        randomv = (EditText) findViewById(R.id.random);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //saveRecord();
        //readRecord();
        //updateRecord();
        //readRecord();
        //deleteRecord();
        //readRecord();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        database.close();
    }

    public void saveRecord(View view)
    {
        String title = title1.getText().toString();//"Record title";
        String subtitle = subtitle1.getText().toString();//"Record subtitle";

        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, title);
        values.put(FeedEntry.COLUMN_NAME_SUBTITLE, subtitle);

        long recordId = database.insert(FeedEntry.TABLE_NAME,null, values);

        if (recordId > 0)
        {
            Log.d(TAG, "saveRecord: Record save.");
        }else {
            Log.d(TAG, "saveRecord: Record not saved");
        }
        title1.setText("");
        subtitle1.setText("");

    }

    public void readRecord(View view)
    {
        showdb1.setText("Result: ");
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_TITLE,
                FeedEntry.COLUMN_NAME_SUBTITLE
        };
        String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArg = {
                "Record title"
        };
        String sortOrder = FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";
        Cursor cursor = database.query(FeedEntry.TABLE_NAME, projection, null,null,null,null,null);
        /*
        /reference for above:
        TABLE_NAME = Table
        projection = property or coulmn reference
        1st null = selection (where)
        2nd Null = values for selection
        3rd Null = group by
        4th Null = Filters
        5th Null = sort order
         */
        while(cursor.moveToNext())
        {
            long entryId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry._ID));
            String entryTitle = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TITLE));
            String entrySubtitle = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_SUBTITLE));
            String entryResult = "\nRecord ID: " + entryId + " Title: " + entryTitle + " Subtitle: " + entrySubtitle;
            Log.d(TAG, "readRecord: id: " + entryId + " title: " + entryTitle + " subtitle: " + entrySubtitle);
            showdb1.append(entryResult);
        }
    }
    public void deleteRecord(View view)
    {
        String selection = FeedEntry.COLUMN_NAME_TITLE + " Like ?";
        String[] selectionArgs = {
                title1.getText().toString()
        };
        int deleted = database.delete(
                FeedEntry.TABLE_NAME,
                selection,
                selectionArgs);
        if (deleted > 0)
        {
            Log.d(TAG, "deleteRecord: record deleted");
        }else {
            Log.d(TAG, "deleteRecord: record not deleted");
        }
    }

    public void updateRecord(View view)
    {
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, randomv.getText().toString()); //Record new title

        String selection = FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = {
                title1.getText().toString()
        };
        String record;
        int count = database.update(FeedEntry.TABLE_NAME, values, selection, selectionArgs);
        if (count > 0)
        {
            record = "updateRecord: Updated records " + count;
            Log.d(TAG, record);
        } else {
            record = "updateRecord: Records not updated";
        Log.d(TAG, record);
            }
            randomv.setText("");
            showdb1.append(record);
    }
}