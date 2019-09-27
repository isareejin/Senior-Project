package com.platform.myapp.myapp;

//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.net.Uri;
//import android.util.Log;
//import android.view.View;
//import android.view.Menu;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//
//
//
//public class CameraActivity extends Activity {
//
//    ImageView imgView;
//    static final int REQUEST_TAKE_PHOTO = 1;
//    String mCurrentPhotoPath;
//
//    static String strSDCardPathName = Environment.getExternalStorageDirectory() + "/Helper_SOS" + "/";
//    private int resultCode;
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        final Button buttonmainphotomail = (Button)findViewById(R.id.buttonmainphotomail);
//
//        //*** Create Folder
//        createFolder();
//
//        //*** ImageView
//        imgView = (ImageView) findViewById(R.id.imgView);
//
//        //*** Take Photo
//        //final Button btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);
//
//        // Perform action on click
//        buttonmainphotomail.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//
//                /// send to email
//                String path = Environment.getExternalStorageDirectory().toString();
//                File file = new File(path,"YourImage.jpg");
//                Uri pngUri = Uri.fromFile(file);
//                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
//                emailIntent.setType("application/image");
//                emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, pngUri);
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"isareejin@gmail.com"});
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Please Help me!!!!!");
//                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//                ///
//
//
//                // Ensure that there's a camera activity to handle the intent
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                    // Create the File where the photo should go
//                    File photoFile = null;
//                    try {
//                        photoFile = createImageFile();
//                    } catch (IOException ex) {}
//                    // Continue only if the File was successfully created
//                    if (photoFile != null) {
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                                Uri.fromFile(photoFile));
//                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//
//                    }
//                }
//            }
//        });
//
//
//
//    }
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "YourImage";
//        File storageDir = new File(strSDCardPathName);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {        //แบบเดิม resultCode == RESULT_OK
//
//	        Bundle extras = data.getExtras();
//	        Bitmap imageBitmap = (Bitmap) extras.get("data");
//	        imgView.setImageBitmap(imageBitmap);
//        }
//    }
//
//
//
//
//    public static void createFolder()
//    {
//        File folder = new File(strSDCardPathName);
//        try
//        {
//            // Create folder
//            if (!folder.exists()) {
//                folder.mkdir();
//            }
//        }catch(Exception ex){}
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        if (resultCode == 0) { //แบบเดิม resultCode == RESULT_OK
//
//            // Get the dimensions of the View
//            //int targetW = 300;
//            //int targetH = 300;
//            int targetW = imgView.getWidth();
//            int targetH = imgView.getHeight();
//
//            // Get the dimensions of the bitmap
//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//            bmOptions.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//            int photoW = bmOptions.outWidth;
//            int photoH = bmOptions.outHeight;
//
//            // Determine how much to scale down the image
//            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//
//            // Decode the image file into a Bitmap sized to fill the View
//            bmOptions.inJustDecodeBounds = false;
//            bmOptions.inSampleSize = scaleFactor;
//            bmOptions.inPurgeable = true;
//
//            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//            imgView.setImageBitmap(bitmap);
//
//        }
//        return true;
//    }
//
//
//
//
//
//}


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import static com.platform.myapp.myapp.myDBClass.TABLE_CONTACTS;


public class CameraActivity extends AppCompatActivity {
    SQLiteDatabase db;
    myDBClass mDatabase;
    ImageView imageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = new myDBClass(this);
        db = mDatabase.getReadableDatabase(); // Read Data

        Button buttonmainphotomail = (Button) findViewById(R.id.buttonmainphotomail);
        imageView = (ImageView) findViewById(R.id.imageView);

        buttonmainphotomail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //////////////////////
               Cursor cursor = db.rawQuery("SELECT * FROM  " + TABLE_CONTACTS, null);
                String Email = "";
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            Email += cursor.getString(cursor.getColumnIndex("Email")) + ",";
                        } while (cursor.moveToNext());
                    }
                }
                cursor.close();
                db.close();
                if (Email.length() > 0) Email = Email.substring(0, Email.length() - 1);
                Log.e("TestShowEmail", Email);
                ///////////////

                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String fileName = "20180817_141719.jpg";
                File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/", fileName);
                Uri path = Uri.fromFile(filelocation);

               // Intent emailIntent = new Intent(Intent.ACTION_SEND);

                Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri.fromParts(
                        "mailto", Email, null));

                emailIntent.setType("application/image");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, Email);
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Test Subject");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "From My App");
                emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));



            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);


    }
}

