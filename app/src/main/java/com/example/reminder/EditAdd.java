package com.example.reminder;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.view.Window;
import android.widget.TextView;

public class EditAdd extends AppCompatActivity {
    int requestCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.55));

        EditText reminderText = (EditText) findViewById(R.id.reminderEditText);
        reminderText.setBackgroundColor(Color.WHITE);
        Intent i = getIntent();
        requestCode = i.getIntExtra("requestCode",0);
        if(requestCode == 2){
            String text = i.getStringExtra("text");
            ConstraintLayout layout = findViewById(R.id.editAddLayout);
            layout.setBackgroundColor(Color.BLUE);

            TextView textView = findViewById(R.id.textView);
            textView.setText("Edit Reminder");

            EditText editText = findViewById(R.id.reminderEditText);
            editText.setText(text,TextView.BufferType.EDITABLE);

        }
        else{
            ConstraintLayout layout = findViewById(R.id.editAddLayout);
            layout.setBackgroundColor(Color.parseColor("#4CAF50"));

            TextView textView = findViewById(R.id.textView);
            textView.setText("Add Reminder");
        }
    }

    public void addItems(View view){
        EditText reminderEditText = (EditText) findViewById(R.id.reminderEditText);
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        boolean isImportant = checkBox.isChecked();
        String reminderText = reminderEditText.getText().toString();
        Intent resultText = new Intent();
        resultText.putExtra("resultText",reminderText);
        resultText.putExtra("isImportant",isImportant);
        setResult(RESULT_OK,resultText);
        finish();

    }

    public void close(View view){
        finish();
    }
}
