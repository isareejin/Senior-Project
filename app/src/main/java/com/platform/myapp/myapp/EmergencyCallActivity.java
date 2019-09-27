package com.platform.myapp.myapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class EmergencyCallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_call);


        Button buttonAboutBack = (Button) findViewById(R.id.buttonback);
        buttonAboutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });



        Button buttonAbout = (Button) findViewById(R.id.button1);

        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:911"));
                startActivity(intent);
            }
        });

        Button buttonAbout2 = (Button) findViewById(R.id.button2);

        buttonAbout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:1154"));
                startActivity(intent);
            }
        });

        Button buttonAbout3 = (Button) findViewById(R.id.button3);

        buttonAbout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:1155"));
                startActivity(intent);
            }
        });

        Button buttonAbout4 = (Button) findViewById(R.id.button4);

        buttonAbout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:1137"));
                startActivity(intent);
            }
        });

        Button buttonAbout5 = (Button) findViewById(R.id.button5);

        buttonAbout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:1193"));
                startActivity(intent);
            }
        });

        Button buttonAbout6 = (Button) findViewById(R.id.button6);

        buttonAbout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:199"));
                startActivity(intent);
            }
        });

        Button buttonAbout7 = (Button) findViewById(R.id.button7);

        buttonAbout7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:022821815"));
                startActivity(intent);
            }
        });

        Button buttonAbout8 = (Button) findViewById(R.id.button8);

        buttonAbout8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:1669"));
                startActivity(intent);
            }
        });

        Button buttonAbout9 = (Button) findViewById(R.id.button9);

        buttonAbout9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:1586"));
                startActivity(intent);
            }
        });

        Button buttonAbout10 = (Button) findViewById(R.id.button10);

        buttonAbout10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:1192"));
                startActivity(intent);
            }
        });



    }


}
