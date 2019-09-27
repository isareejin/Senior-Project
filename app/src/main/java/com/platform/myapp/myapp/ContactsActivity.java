package com.platform.myapp.myapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ContactsActivity extends Activity {

    String[] Cmd = {"Edit","Delete"};
    ArrayList<HashMap<String, String>> ContactList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        // btnBack (Back)
        final Button back = (Button) findViewById(R.id.buttonback);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open Form Main
                Intent newActivity = new Intent(ContactsActivity.this,MainActivity.class);
                startActivity(newActivity);
            }
        });

        // btnAdd (Add)
        final Button add = (Button) findViewById(R.id.buttonadd);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open Form Main
                Intent newActivity = new Intent(ContactsActivity.this,AddContactActivity.class);
                startActivity(newActivity);
            }
        });




        // get Data from SQLite
        myDBClass myDb = new myDBClass(this);
        ContactList = myDb.SelectAllData();

        // listView1
        ListView lisView1 = (ListView)findViewById(R.id.listView1);

        SimpleAdapter sAdap;
        sAdap = new SimpleAdapter(ContactsActivity.this, ContactList, R.layout.activity_column,
                new String[] {"ContactID", "Name", "Email", "Tel"}, new int[] {R.id.ColContactID, R.id.ColName, R.id.ColEmail, R.id.ColTel});
        lisView1.setAdapter(sAdap);
        registerForContextMenu(lisView1);

        // show contact detail
        lisView1.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                // Show on new activity
                Intent newActivity = new Intent(ContactsActivity.this,DetailContactActivity.class);
                newActivity.putExtra("ConID", ContactList.get(position).get("ContactID").toString());
                startActivity(newActivity);

            }
        });

    }

    // Show List data
    public void ShowListData()
    {
        myDBClass myDb = new myDBClass(this);
        ContactList = myDb.SelectAllData();

        // listView1
        ListView lisView1 = (ListView)findViewById(R.id.listView1);

        SimpleAdapter sAdap;
        sAdap = new SimpleAdapter(ContactsActivity.this, ContactList, R.layout.activity_column,
                new String[] {"ContactID", "Name", "Email", "Tel"}, new int[] {R.id.ColContactID, R.id.ColName, R.id.ColEmail, R.id.ColTel});
        lisView1.setAdapter(sAdap);
        registerForContextMenu(lisView1);
    }


    // pop-up delete & edit
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderIcon(android.R.drawable.btn_star_big_on);
        menu.setHeaderTitle("Command for : [" + ContactList.get(info.position).get("Name").toString() + "]");
        String[] menuItems = Cmd;
        for (int i = 0; i<menuItems.length; i++) {
            menu.add(Menu.NONE, i, i, menuItems[i]);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = Cmd;
        String CmdName = menuItems[menuItemIndex];
        String ConID = ContactList.get(info.position).get("ContactID").toString();

        myDBClass myDb = new myDBClass(this);
        ContactList = myDb.SelectAllData();

        // listView1
        ListView lisView1 = (ListView)findViewById(R.id.listView1);


        // Check Event Command
        if ("Edit".equals(CmdName)) {

                    // Show on new activity
                    Intent newActivity = new Intent(ContactsActivity.this,UpdateContactActivity.class);
                    newActivity.putExtra("ConID", ContactList.get(info.position).get("ContactID").toString());
                    startActivity(newActivity);

        }

        else if ("Delete".equals(CmdName)) {
            //myDBClass myDb = new myDBClass(this);

            long flg = myDb.DeleteData(ConID);
            if(flg > 0)
            {
                Toast.makeText(ContactsActivity.this,"Delete Data Successfully",
                        Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(ContactsActivity.this,"Delete Data Failed.",
                        Toast.LENGTH_LONG).show();
            }

            // Call Show Data again
            ShowListData();
        }

        return true;
    }

}
