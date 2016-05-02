package com.cpp.photovsphoto.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.AWSConfiguration;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.content.TransferHelper;
import com.amazonaws.mobile.content.UserFileManager;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.cpp.photovsphoto.R;
import com.amazonaws.ClientConfiguration;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_analysis_result.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_analysis_result#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_analysis_result extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TransferHelper transferHelper;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private UserFileManager userFileManager;
    final ClientConfiguration clientConfiguration = new ClientConfiguration();
    private String bucket = AWSConfiguration.AMAZON_S3_USER_FILES_BUCKET;
    private IdentityManager identityManager;
    private AmazonS3Client s3 = null;
    //s3.setRegion(Region.getRegion(Regions.MY_BUCKET_REGION));
    //TransferUtility transferUtility = new TransferUtility(s3, APPLICATION_CONTEXT);

    private OnFragmentInteractionListener mListener;

    public fragment_analysis_result() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_analysis_result.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_analysis_result newInstance(String param1, String param2) {
        fragment_analysis_result fragment = new fragment_analysis_result();
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

        AWSMobileClient.defaultMobileClient()
                .createUserFileManager(bucket, "public",
                        new UserFileManager.BuilderResultHandler() {
                            @Override
                            public void onComplete(final UserFileManager userFileManager) {
                                if (!isAdded()) {
                                    userFileManager.destroy();
                                    return;
                                }

                                fragment_analysis_result.this.userFileManager = userFileManager;

                            }
                        });
        identityManager = new IdentityManager(getActivity(),clientConfiguration);
        s3 = new AmazonS3Client(identityManager.getCredentialsProvider(), clientConfiguration);
        clientConfiguration.setUserAgent(AWSConfiguration.AWS_MOBILEHUB_USER_AGENT);

    }
    TextView textViewStatus;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analysis_result, container, false); //do it this way otherwise calls waitforresponse too soon.
        WaitForResponse(); //TODO:Still notworking;
        return view;
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
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void WaitForResponse(){ //currently just attempt to list items
        /*
        ObjectListing listing = s3.listObjects( bucket, "" ); //blank prefix
        List<S3ObjectSummary> summaries = listing.getObjectSummaries();

        while (listing.isTruncated()) {
            listing = s3.listNextBatchOfObjects (listing);
            summaries.addAll (listing.getObjectSummaries());
        }
        textViewStatus.setText(listing.toString());*/
        textViewStatus = (TextView) getView().findViewById(R.id.textViewStatus);
        textViewStatus.setText("You're a faggot Harry");
        //transferHelper.download(String filePath, long fileSize, ContentProgressListener listener);
    }




}
