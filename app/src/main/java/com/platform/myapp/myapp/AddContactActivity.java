package com.platform.myapp.myapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContactActivity extends Activity  {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);


        // btnSave (Save)
        final Button save = (Button) findViewById(R.id.btnSave);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // If Save Complete
                if(SaveData())
                {
                    // Open Form Main
                    Intent newActivity = new Intent(AddContactActivity.this,ContactsActivity.class);
                    startActivity(newActivity);
                }
            }
                });


                // btnCancel (Cancel)
                final Button cancel = (Button) findViewById(R.id.btnCancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Open Form Main
                        Intent newActivity = new Intent(AddContactActivity.this,ContactsActivity.class);
                        startActivity(newActivity);
                    }
                });

    }

    public boolean SaveData()
    {
        // txtContactID, txtName, tEmail, txtTel
        final EditText tContactID = (EditText) findViewById(R.id.txtContactID);
        final EditText tName = (EditText) findViewById(R.id.txtName);
        final EditText tEmail = (EditText) findViewById(R.id.txtEmail);
        final EditText tTel = (EditText) findViewById(R.id.txtTel);


//        Intent intentMail=new Intent(this,SosActivity.class);
//        intentMail.putExtra("tEmail", tEmail.getText().toString());
//        startActivity(intentMail); //หน้าส่งค่าไม่มีอะไรเปลี่ยน

//        Log.d("MainActivity", "rrrrrrrrrrrrrrrrrrrr : " + tEmail);

        // Dialog
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        AlertDialog ad = adb.create();

        // Check ContactID
        if(tContactID.getText().length() == 0)
        {
            ad.setMessage("Please input [ContactID] ");
            ad.show();
            tContactID.requestFocus();
            return false;
        }

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

        // Check Data (ContactID exists)
        String arrData[] = myDb.SelectData(tContactID.getText().toString());
        if(arrData != null)
        {
            ad.setMessage("ContactID already exists!  ");
            ad.show();
            tContactID.requestFocus();
            return false;
        }

        // Save Data
        long saveStatus = myDb.InsertData(tContactID.getText().toString(),
                tName.getText().toString(),
                tEmail.getText().toString(),
                tTel.getText().toString());
        if(saveStatus <=  0)
        {
            ad.setMessage("Error!! ");
            ad.show();
            return false;
        }

        Toast.makeText(AddContactActivity.this,"Add Data Successfully. ",
                Toast.LENGTH_SHORT).show();

        return true;
    }

}