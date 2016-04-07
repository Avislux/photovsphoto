package com.cpp.photovsphoto.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.amazonaws.mobile.content.UserFileManager;
import com.amazonaws.mobile.util.ImageSelectorUtils;
import com.amazonaws.services.s3.internal.Constants;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cpp.photovsphoto.R;
import com.cpp.photovsphoto.demo.content.ContentListItem;
import com.cpp.photovsphoto.demo.content.ContentListViewAdapter;
import com.cpp.photovsphoto.navigation.FragmentBase;

import java.io.File;
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
public class fragment_analyze extends FragmentBase {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LOG_TAG = "AnalyzeActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    }
    Button goButton;
    Button uploadButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analyze, container, false);
        goButton = (Button) view.findViewById(R.id.buttonGo);
        uploadButton = (Button) view.findViewById(R.id.buttonUpload);
        goButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            Log.d("AnalyzeFragment: ", "startActivityforResult got called");
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //TODO: This bit doesn't work
        Log.d("AnalyzeFragment: ", "onActivityResult got called");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Log.d("AnalyzeFragment: ", "got into the if block");
            String toastText = "onActivityResult called";
            Toast myToast = Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG);
            Intent takePictureIntent = getActivity().getIntent();
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = (ImageView) getView().findViewById(R.id.imageViewPicture);
            imageView.setImageBitmap(imageBitmap); //set thumbnail to photo

        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException { // this gets called when the camera app opens ;creates temporary file
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
        mCurrentPhotoPath = image.getAbsolutePath();
        String toastText = "Image saved " + image.getAbsolutePath();
        Toast myToast = Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG);
        myToast.show();
        Log.d(LOG_TAG, mCurrentPhotoPath);
        return image;
    }

    public void onClickThumbnail(View v) {
        //expands iamge
    }
    private UserFileManager userFileManager;
    private ContentListViewAdapter contentListItems;
    public void onClickUpload() {
        //upload to aws
        //TODO: Fix this
       // PutObjectRequest por = new PutObjectRequest( Constants.getPictureBucket(), Constants.PICTURE_NAME, new java.io.File( filePath) );
        //s3Client.putObject( por );

        String path = mCurrentPhotoPath;
        Log.d(LOG_TAG, "file path: " + path);
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle(R.string.content_progress_dialog_title_wait);
        dialog.setMessage(
                getString(R.string.user_files_browser_progress_dialog_message_upload_file,
                        path));
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
       // dialog.setMax((int) new File(path).length());
        dialog.setCancelable(false);
        dialog.show();

        File file = new File(path); //TODO: null pointers here
        userFileManager.uploadContent(file, file.getName(), new ContentProgressListener() {
            @Override
            public void onSuccess(final ContentItem contentItem) {
                contentListItems.add(new ContentListItem(contentItem));
                contentListItems.sort(ContentListItem.contentAlphebeticalComparator);
                contentListItems.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onProgressUpdate(final String fileName, final boolean isWaiting,
                                         final long bytesCurrent, final long bytesTotal) {
                dialog.setProgress((int) bytesCurrent);
            }

            @Override
            public void onError(final String fileName, final Exception ex) {
                dialog.dismiss();
                showError(R.string.user_files_browser_error_message_upload_file,
                        ex.getMessage());
            }
        });
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
        Log.d(LOG_TAG, "galleryaddpic "+ contentUri.toString());
        Toast myToast = Toast.makeText(getActivity(), "galleryaddpic called", Toast.LENGTH_LONG);
        myToast.show();

    }
    private void showError(final int resId, Object... args) {
        new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(getString(resId, (Object[]) args))
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}