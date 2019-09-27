package com.platform.myapp.myapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateContactActivity extends Activity  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        // Read var from Intent
        Intent intent= getIntent();
        final String ConID = intent.getStringExtra("ConID");

        // Show Data
        ViewData(ConID);

        // btnSave (Save)
        final Button save = (Button) findViewById(R.id.btnSave);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // If Save Complete
                if(UpdateData(ConID))
                {
                    // Open Form ListUpdate
                    Intent newActivity = new Intent(UpdateContactActivity.this,ContactsActivity.class);
                    startActivity(newActivity);
                }
            }
        });

        // btnCancel (Cancel)
        final Button cancel = (Button) findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open Form ListUpdate
                Intent newActivity = new Intent(UpdateContactActivity.this,ContactsActivity.class);
                startActivity(newActivity);
            }
        });

    }

    public void ViewData(String ConID)
    {
        // txtMemberID, txtName, txtContact, txtTel
        final TextView tContactID = (TextView) findViewById(R.id.txtContactID);
        final EditText tName = (EditText) findViewById(R.id.txtName);
        final EditText tEmail = (EditText) findViewById(R.id.txtEmail);
        final EditText tTel = (EditText) findViewById(R.id.txtTel);

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

    public boolean UpdateData(String ConID)
    {

        // txtName, txtEmail, txtTel
        final EditText tName = (EditText) findViewById(R.id.txtName);
        final EditText tEmail = (EditText) findViewById(R.id.txtEmail);
        final EditText tTel = (EditText) findViewById(R.id.txtTel);

        // Dialog
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        AlertDialog ad = adb.create();

        // Check Name
        if(tName.getText().length() == 0)
        {
            ad.setMessage("Please input [Name] ");
            ad.show();
            tName.requestFocus();
            return false;
        }

        // Check Email
        if(tEmail.getText().length() == 0)
        {
            ad.setMessage("Please input [Email] ");
            ad.show();
            tEmail.requestFocus();
            return false;
        }

        // Check Tel
        if(tTel.getText().length() == 0)
        {
            ad.setMessage("Please input [Tel] ");
            ad.show();
            tTel.requestFocus();
            return false;
        }

        // new Class DB
        final myDBClass myDb = new myDBClass(this);

        // Save Data
        long saveStatus = myDb.UpdateData(ConID,
                tName.getText().toString(),
                tEmail.getText().toString(),
                tTel.getText().toString());
        if(saveStatus <=  0)
        {
            ad.setMessage("Error!! ");
            ad.show();
            return false;
        }

        Toast.makeText(UpdateContactActivity.this,"Update Data Successfully. ",
                Toast.LENGTH_SHORT).show();

        return true;

    }

}
