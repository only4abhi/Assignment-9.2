package com.example.abhishek.assignment92;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    //define global views variable
    public ImageView imageView;
    public Button selectImage, uploadImage;
    private static final int RESULT_SELECT_IMAGE = 1;
    public String timestamp;
    Context context;

    public final static String APP_PATH_SD_CARD = "/Card/";
    public final static String APP_THUMBNAIL_PATH_SD_CARD = "thumbnails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate view
        imageView = (ImageView) findViewById(R.id.imageView);
        selectImage = (Button) findViewById(R.id.selectImage);
        uploadImage = (Button) findViewById(R.id.uploadImage);

        //when selectImage button is pressed
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                selectImage();
            }
        });

        //when uploadImage button is pressed
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get image in bitmap format
                Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                //upload the image to external data card
                saveImageToExternalStorage(image);
            }
        });
    }

//Upload Image in External Folder
    public boolean saveImageToExternalStorage(Bitmap image) {
      // String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_PATH_SD_CARD + APP_THUMBNAIL_PATH_SD_CARD;
       // File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"images");directory.mkdirs();
        File filepath = Environment.getExternalStorageDirectory();

        try {
            File dir = new File(filepath.getAbsolutePath() + "/thumbnails");
            if (!dir.exists()) {
                dir.mkdirs();
            }


            OutputStream fOut = null;
            File file = new File(dir, "mycheck.png");
            //Toast.makeText(MainActivity.this, "Image Saved to SD Card", Toast.LENGTH_SHORT).show();


            file.createNewFile();
            fOut = new FileOutputStream(file);

// 100 means no compression, the lower you go, the stronger the compression
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

            return true;

        } catch (Exception e) {
            Log.e("saveToExternalStorage()", e.getMessage());
            return false;
        }
    }


    //function to select a image
    private void selectImage(){
        //open album to select image
        Intent gallaryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallaryIntent, RESULT_SELECT_IMAGE);
    }

    /*
    * This function is called when we pick some image from the album
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_SELECT_IMAGE && resultCode == RESULT_OK && data != null){
            //set the selected image to image variable
            Uri image = data.getData();
            imageView.setImageURI(image);

            //get the current timeStamp and strore that in the time Variable
            Long tsLong = System.currentTimeMillis() / 1000;
            timestamp = tsLong.toString();

            Toast.makeText(getApplicationContext(),timestamp,Toast.LENGTH_SHORT).show();
        }
    }
}
