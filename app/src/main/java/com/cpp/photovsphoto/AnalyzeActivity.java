package com.cpp.photovsphoto;
//not in use
/*
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.cpp.photovsphoto.navigation.FragmentBase;

import java.io.File;
import java.io.IOException;


//originally extends FragmentBase/AppCompatActivity
public class AnalyzeActivity extends AppCompatActivity {
    /**
     * Logging tag for this class.
     *
    private static final String LOG_TAG = "AnalyzeActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;


    /**
     * This fragment's view.
     *
    private View mFragmentView;

    /**
     * Text view for showing the user identity.
     *
    private TextView userIdTextView;

    /**
     * Text view for showing the user name.
     *
    private TextView userNameTextView;

    /**
     * Image view for showing the user image.
     *
    private ImageView userImageView;

    /*
            @Override /
            public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                     final Bundle savedInstanceState) {

                // Inflate the layout for this fragment
                mFragmentView = inflater.inflate(R.layout.activity_analyze, container, false);
                //userNameTextView = (TextView) mFragmentView.findViewById(R.id.textView_demoIdentityUserName);
                //userIdTextView = (TextView) mFragmentView.findViewById(R.id.textView_demoIdentityUserID);
                //userImageView = (ImageView)mFragmentView.findViewById(R.id.imageView_demoIdentityUserImage);


                return mFragmentView;
            }

            @Override
            public void onDestroyView() {
                super.onDestroyView();

            }
            *
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
    }

    public void onClickGo(View v) {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() { //opens camera app
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();

            galleryAddPic();
        } catch (IOException ex) {
            // Error occurred while creating the File
            ex.printStackTrace();
        }
        if (photoFile != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        }
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Intent takePictureIntent = getIntent();
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = (ImageView) findViewById(R.id.imageViewPicture);
            imageView.setImageBitmap(imageBitmap); //set thumbnail to photo

        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        String toastText = "Image saved " + image.getAbsolutePath();
        Toast myToast = Toast.makeText(this, toastText, Toast.LENGTH_LONG);
        myToast.show();
        return image;
    }

    public void onClickThumbnail(View v) {
        //expands iamge
    }

    public void onClickUpload(View v) {
        //upload to aws
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Toast myToast = Toast.makeText(this, "Added to gallery but not really", Toast.LENGTH_LONG);
        myToast.show();
    }
}*/