package com.cpp.photovsphoto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.cpp.photovsphoto.navigation.FragmentBase;

/**
 * Created by Jonathan on 2/29/2016.
 */
public class AnalyzeActivity extends FragmentBase {
    /** Logging tag for this class. */
    private static final String LOG_TAG ="AnalyzeActivity";



    /** This fragment's view. */
    private View mFragmentView;

    /** Text view for showing the user identity. */
    private TextView userIdTextView;

    /** Text view for showing the user name. */
    private TextView userNameTextView;

    /** Image view for showing the user image. */
    private ImageView userImageView;

    @Override //TODO: Copy code from other repo
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
}
