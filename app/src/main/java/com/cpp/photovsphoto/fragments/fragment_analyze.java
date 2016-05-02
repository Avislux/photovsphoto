package com.cpp.photovsphoto.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.amazonaws.mobile.content.TransferHelper;
import com.amazonaws.mobile.content.UserFileManager;
import com.amazonaws.mobile.util.ImageSelectorUtils;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.internal.Constants;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.mobile.AWSConfiguration;
import com.cpp.photovsphoto.R;
import com.cpp.photovsphoto.demo.UserFilesBrowserFragment;
import com.cpp.photovsphoto.demo.content.ContentListItem;
import com.cpp.photovsphoto.demo.content.ContentListViewAdapter;
import com.cpp.photovsphoto.navigation.FragmentBase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_analyze.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_analyze#newInstance} factory method to
 * create an instance of this fragment.
 */

public class fragment_analyze extends FragmentBase  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LOG_TAG = "AnalyzeActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private UserFileManager userFileManager;
    private String bucket;
    private String prefix;
    public static final String BUNDLE_ARGS_S3_BUCKET = "bucket";
    public static final String BUNDLE_ARGS_S3_PREFIX = "prefix";
    private OnFragmentInteractionListener mListener;

    public fragment_analyze() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment analyze.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_analyze newInstance(String param1, String param2) {
        fragment_analyze fragment = new fragment_analyze();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        bucket = AWSConfiguration.AMAZON_S3_USER_FILES_BUCKET;
        prefix = "prefix";
        //final ProgressDialog dialog = getProgressDialog(R.string.content_progress_dialog_message_load_local_content);
        AWSMobileClient.defaultMobileClient()
                .createUserFileManager(bucket, "public",
                        new UserFileManager.BuilderResultHandler() {
                            @Override
                            public void onComplete(final UserFileManager userFileManager) {
                                if (!isAdded()) {
                                    userFileManager.destroy();
                                    return;
                                }

                                fragment_analyze.this.userFileManager = userFileManager;
                                //createContentList(getView(), userFileManager);
                                //userFileManager.setContentRemovedListener(contentListItems);
                                //dialog.dismiss();
                                //refreshContent(currentPath);
                            }
                        });
    }
    Button goButton;
    Button uploadButton;
    Button chooseFileButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analyze, container, false);
        goButton = (Button) view.findViewById(R.id.buttonGo);
        uploadButton = (Button) view.findViewById(R.id.buttonUpload);
        chooseFileButton = (Button) view.findViewById(R.id.buttonChooseFile);
        goButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(); //opencamera app when clicked (1)
            }
        });
        chooseFileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onChooseFile();
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onClickUpload();
            }
        });
        return view ;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other com.cpp.photovsphoto.fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    static String gFileName;
    //String mCurrentPhotoPath;
    private Uri globalUri;
    //private Uri fileUri;
    private void dispatchTakePictureIntent() { //opens camera app (2)
        Uri tempfileUri;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        tempfileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        String tempfileUriString = tempfileUri.getPath();

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, "data"); //key, value
        //takePictureIntent.putExtra("temporaryFileUri",tempfileUriString); //nope not working

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            Log.d("AnalyzeFragment: ", "startActivityforResult got called");
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
       // if (photoFile != null) {//  }




        Toast.makeText(getActivity(), "temp file " + tempfileUri, Toast.LENGTH_SHORT).show();
        Log.d("AnalyzeFragment: ", "dispatchtakepictureintent " + tempfileUriString);
    }
    private static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 93;
    private void onChooseFile(){
        final Intent intent = ImageSelectorUtils.getImageSelectionIntent();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, "data");
        startActivityForResult(intent, 0);


        //alternatively you can set file filter
        //intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { "png" });


    }
    String gPath = null;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //(3)
        Log.d("AnalyzeFragment: ", "onActivityResult got called");
        Uri tempUri;
        TextView textView = (TextView) getView().findViewById(R.id.textViewFileName);
        if (resultCode == Activity.RESULT_OK) {
            tempUri = data.getData(); //OMFG

            if (requestCode == REQUEST_IMAGE_CAPTURE ) {
                Log.d("AnalyzeFragment: ", "Back from camera intent");
                Bundle extras = data.getExtras();

                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ImageView imageView = (ImageView) getView().findViewById(R.id.imageViewPicture); //set imageview thumbnail
                imageView.setImageBitmap(imageBitmap); //set thumbnail to photo

                //Intent takePictureIntent = getActivity().getIntent();

                //String tempstring = getActivity().getIntent().getStringExtra("temporaryFileUri");
                // String tempstring = extras.getString("temporaryFileUri");
                //Log.d("AnalyzeFragment: ", "path " + tempstring);
                //tempUri = Uri.parse(extras.getString("temporaryFileUri"));
                globalUri = tempUri;
                //tempUri = globalUri;
                //mCurrentPhotoPath = fileUri.toString();
                String toastText = "onactivityresult: " + tempUri;
                Toast myToast = Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG);
               // myToast.show();
                galleryAddPic();

                File image = new File(tempUri.toString());
                String absolutePath = image.getAbsolutePath();
                Log.d("AnalyzeFragment: ", "uri " + tempUri);

                FileOutputStream out = null;

                try {
                    out = new FileOutputStream(tempUri.toString());
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    //out.write(imageBitmap);// bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                    Log.d("AnalyzeFragment: ", "image should be saved, compressed to" + tempUri);
                } catch (FileNotFoundException e) {
                    Log.d("AnalyzeFragment: ", "File not found: " + e.getMessage());
                } catch (Exception e) {
                    Log.d("AnalyzeFragment: ", "Error accessing file: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                            Log.d("AnalyzeFragment: ", "file close");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("AnalyzeFragment: ", "HIHIHIHIH");
                Log.d("AnalyzeFragment: ", "absolute path: " + absolutePath);
                Log.d("AnalyzeFragment: ", tempUri.toString() + gFileName);

            }
            else {
                Log.d("AnalyzeFragment: ", " back from file picker intent");
                final String path = ImageSelectorUtils.getFilePathFromUri(getActivity(), tempUri);
                Log.d(LOG_TAG, "file path: " + path);
                globalUri = tempUri;
                gPath = path;
                textView.setText(path);
                //TODO: set image from this

            }
        }


    }




    public void onClickThumbnail(View v) {
        //expands iamge
    }


    public void onClickUpload() {
        //upload to aws
        if(gPath == null){
            Log.d(LOG_TAG, "Upload: Path is null");
            final AlertDialog.Builder nullPath = new AlertDialog.Builder(getActivity());
            nullPath.setTitle(R.string.textNoFileSelected);
            nullPath.setMessage(R.string.textNoFileSelected);
            nullPath.setCancelable(true);
            nullPath.show();
            nullPath.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogNull, int id) {
                    dialogNull.cancel();

                }
            });
        }
        else {

            Log.d(LOG_TAG, "onclickupload file path: " + gPath);
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            final AlertDialog.Builder completeDialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle(R.string.content_progress_dialog_title_wait);
            dialog.setMessage(
                    getString(R.string.user_files_browser_progress_dialog_message_upload_file,
                            gPath));
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            dialog.setCancelable(true);
            dialog.show();

            File file = new File(gPath);


            userFileManager.uploadContent(file, file.getName(), new ContentProgressListener() {
                @Override
                public void onSuccess(final ContentItem contentItem) {
                    Log.d("AnalyzeFragment: ", "Upload successful");
                    dialog.dismiss();
                    completeDialog.setTitle(R.string.textUploadComplete);
                    completeDialog.setMessage(R.string.textUploadComplete);
                    completeDialog.setCancelable(true);
                    completeDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog1, int id) {
                            dialog1.cancel();
                            WaitForResponse();
                        }
                    });
                    AlertDialog uploadCompleteAlert = completeDialog.create();
                    uploadCompleteAlert.show();
                }

                @Override
                public void onProgressUpdate(final String fileName, final boolean isWaiting,
                                             final long bytesCurrent, final long bytesTotal) {
                    dialog.setProgress((int) bytesCurrent);
                    Log.d("AnalyzeFragment: ", "Upload update");
                }

                @Override
                public void onError(final String fileName, final Exception ex) {
                    dialog.dismiss();
                    Log.d("AnalyzeFragment: ", "upload error");
                    showError(R.string.user_files_browser_error_message_upload_file,
                            ex.getMessage());
                }
            });
        }
    }
    private void WaitForResponse(){ //inflate new fragment
        FragmentActivity activity = this.getActivity();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, new fragment_analysis_result(), "Results")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
    private void galleryAddPic() { //TODO:Verify add to gallery
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(globalUri.toString());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
        Log.d(LOG_TAG, "galleryaddpic "+ contentUri.toString());
        //Toast myToast = Toast.makeText(getActivity(), "galleryaddpic called", Toast.LENGTH_SHORT);
        //myToast.show();

    }
    private void showError(final int resId, Object... args) {
        new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(getString(resId, (Object[]) args))
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        gFileName = "IMG_"+ timeStamp + ".jpg";
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
    private File createImageFile() throws IOException { // this gets called when the camera app opens ;root/sdcard/pictures
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
        //mCurrentPhotoPath = image.getAbsolutePath();
        String toastText = "Image created " + image.getAbsolutePath();
        Toast myToast = Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT);
        myToast.show();
        //Log.d(LOG_TAG, mCurrentPhotoPath);
        return image;
    }

}