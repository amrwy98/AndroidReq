package com.example.reminder;

import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //THIS IS THE LIST VIEW OF REMINDERS WITH WHICH YOU'LL CONNECT OUR DATABASE
    ////////////////
    ListView myView;
    ////////////////



    //THIS IS THE ARRAYLIST WHICH WILL BE REPLACED WITH OUR DATABASE
    ////////////////////////////////
    //ArrayList<String> myReminders;
    //Cursor myRems;
    ////////////////////////////////

    //THIS IS THE ARRAYADAPTER I USED TO CONNECT THE ARRAYLIST WITH THE LIST VIEW,IN YOUR CASE, YOU'LL USE SOMETHING CALLED CURSOR ADAPTER
    /////////////////////////////////////
    //TODO: Is Important law fel edit 5ala 7aga mesh important
    /////////////////////////////////////
    RemindersDbAdapter remindersDbAdapter;
    RemindersSimpleCursorAdapter remindersSimpleCursorAdapter;
    Cursor cursor;
    ListView alertView;
    int listItemIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //-----------DB Initialization---------
        remindersDbAdapter = new RemindersDbAdapter(this);
        remindersDbAdapter.open();
        //-------------------------------------
        //-------------------------------------
        //ArrayAdapter<String> arrayAdapter;
        cursor = remindersDbAdapter.fetchAllReminders();
        remindersSimpleCursorAdapter = new RemindersSimpleCursorAdapter(this,
                R.layout.row,cursor,new String[] {RemindersDbAdapter.COL_CONTENT},new int[]{R.id.content},0);

        myView = (ListView) findViewById(R.id.myListView);

        //INITIALIZING THE ARRAYLIST AND CONNECTING IT WITH THE ARRAYADAPTER
        /////////////////////////////
        //myReminders = new ArrayList<String>();
        //arrayAdapter = new ArrayAdapter<String>(this, R.layout.row, R.id.content, myReminders);
        /////////////////////////////

        //CONNECTING THE ADAPTER WITH THE LIST VIEW
        ///////////////////////////////////////
        //myView.setAdapter(arrayAdapter);
        //TODO: hena zabat el adapter enno yeb2a lel Cursor
        myView.setAdapter(remindersSimpleCursorAdapter);
        ///////////////////////////////////////

        //Alert Dialog ListView (Leave it as it is)
        alertView = new ListView((this));
        ArrayList<String> alertList = new ArrayList<String>();
        alertList.add("Edit Reminder");
        alertList.add("Delete Reminder");
        ArrayAdapter<String> alertAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,alertList);
        alertView.setAdapter(alertAdapter);

        //Alert Dialog(leave it as it is)
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(true);
        builder.setView(alertView);
        final AlertDialog dialog = builder.create();

        //When user clicks on an item in the alertView list view
        alertView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    View row = myView.getChildAt(listItemIndex);
                    TextView textField = (TextView) row.findViewById(R.id.content);
                    String text = textField.getText().toString();
                    Intent i = new Intent(MainActivity.this,EditAdd.class);
                    i.putExtra("requestCode",2);
                    i.putExtra("text",text);
                    startActivityForResult(i,2);
                    dialog.cancel();
                }
                else{
                    //DELETE THE ITEM FROM THE DATABASE
                    //NOTE THAT THE VARIABLE (listitemindex) WILL CONTAIN THE INDEX OF THE ITEM TO BE DELETED IN THE LIST VIEW
                    ///////////////////////////////////////

                    //TODO:DELETE ITEM HERE
                    cursor.moveToPosition(listItemIndex);
                    Reminder reminder = remindersDbAdapter.fetchReminderById(cursor.getInt(0));
                    remindersDbAdapter.deleteReminderById(reminder.getId());
                    cursor=remindersDbAdapter.fetchAllReminders();
                    cursor.moveToFirst();
                    remindersSimpleCursorAdapter = new RemindersSimpleCursorAdapter(MainActivity.this,
                            R.layout.row,
                            cursor,
                            new String[] {RemindersDbAdapter.COL_CONTENT},
                            new int[]{R.id.content},
                            0);
                    myView.setAdapter(remindersSimpleCursorAdapter);
                    //////////////////////////////////////
                    dialog.cancel();
                }
            }
        });

        myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listItemIndex = position;
                dialog.show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add){
            startActivityForResult(new Intent(MainActivity.this,EditAdd.class),1);
        }
        else if(item.getItemId() == R.id.exit){
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String reminderText = data.getStringExtra("resultText");
                boolean isImportant = data.getBooleanExtra("isImportant",false);

                //HERE YOU'LL ADD THE ITEM TO YOUR DATABASE AND TAKE ANY NECESSARY STEPS (if there are any) TO MAKE IT APPEAR IN THE LISTVIEW
                //YOU CAN DELETE THESE 2 FUNCTION CALLS AS THEY ARE RELATED TO THE ARRAY IMPLEMENTATION
                ////////////////////////////////////////////
                //TODO: ADD
                remindersDbAdapter.createReminder(reminderText,isImportant);
                //myReminders.add(reminderText);
                cursor=remindersDbAdapter.fetchAllReminders();
                cursor.moveToFirst();
                remindersSimpleCursorAdapter = new RemindersSimpleCursorAdapter(MainActivity.this,
                        R.layout.row,
                        cursor,
                        new String[] {RemindersDbAdapter.COL_CONTENT},
                        new int[]{R.id.content},
                        0);
                myView.setAdapter(remindersSimpleCursorAdapter);
                ////////////////////////////////////////////
            }
        }

        else if(requestCode == 2){
            if(resultCode == RESULT_OK){
                String reminderText = data.getStringExtra("resultText"); //this is the new string to be added to the database
                boolean isImportant = data.getBooleanExtra("isImportant",false); //this indicates whether he checked the checkbox or not
                //TODO: EDIT
                //(HERE YOU WILL EDIT THE EXISTING TEXT RATHER THAN ADD A NEW ONE)
                //NOTE THAT THE VARIABLE (listitemindex) WILL CONTAIN THE INDEX OF THE ITEM TO BE EDITED IN THE LIST VIEW
                ////////////////////////////////////////////
                cursor.moveToPosition(listItemIndex);
                Reminder reminder = remindersDbAdapter.fetchReminderById(cursor.getInt(0));
                reminder.setContent(reminderText);
                reminder.setImportant(isImportant?1:0);
                remindersDbAdapter.updateReminder(reminder);
                cursor=remindersDbAdapter.fetchAllReminders();
                cursor.moveToFirst();
                remindersSimpleCursorAdapter = new RemindersSimpleCursorAdapter(MainActivity.this,
                        R.layout.row,
                        cursor,
                        new String[] {RemindersDbAdapter.COL_CONTENT},
                        new int[]{R.id.content},
                        0);
                myView.setAdapter(remindersSimpleCursorAdapter);
                ////////////////////////////////////////////
            }
        }
    }
}



//Backup
//View row = myView.getChildAt(myView.getLastVisiblePosition() );
//TextView corner = (TextView) row.findViewById(R.id.name);
//corner.setBackgroundColor(Color.RED);