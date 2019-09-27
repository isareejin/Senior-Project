package com.platform.myapp.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailContactActivity extends Activity  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        // Read var from Intent
        Intent intent= getIntent();
        String ConID = intent.getStringExtra("ConID");

        // Show Data
        ShowData(ConID);

        // btnCancel (Cancel)
        final Button cancel = (Button) findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open Form Show
                Intent newActivity = new Intent(DetailContactActivity.this,ContactsActivity.class);
                startActivity(newActivity);
            }
        });

    }

    public void ShowData(String ConID)
    {
        // txtContactID, txtName, txtEmail, txtTel
        final TextView tContactID = (TextView) findViewById(R.id.txtContactID);
        final TextView tName = (TextView) findViewById(R.id.txtName);
        final TextView tEmail = (TextView) findViewById(R.id.txtEmail);
        final TextView tTel = (TextView) findViewById(R.id.txtTel);

        // new Class DB
        final myDBClass myDb = new myDBClass(this);

        // Show Data
        String arrData[] = myDb.SelectData(ConID);
        if(arrData != null)
        {
            tContactID.setText(arrData[0]);
            tName.setText(arrData[1]);
            tEmail.setText(arrData[2]);
            tTel.setText(arrData[3]);
        }

    }

}