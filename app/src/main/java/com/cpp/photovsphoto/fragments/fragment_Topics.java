package com.cpp.photovsphoto.fragments;
//trying to push fragments.
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cpp.photovsphoto.R;
import com.cpp.photovsphoto.Versus;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_Topics.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_Topics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_Topics extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public fragment_Topics() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Topics.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_Topics newInstance(String param1, String param2) {
        fragment_Topics fragment = new fragment_Topics();
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
    Button officeButton;
    Button kitchenButton;
    Button foodButton;
    Button petItemsButton;
    Button bcGamesButton;
    Button drinksButton;
    Button sidesButton;
    Button dessertsButton;
    Button roadTripButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("Topics: ", "Inflated");
        View view = inflater.inflate(R.layout.fragment_topics, container, false);
        officeButton = (Button) view.findViewById(R.id.buttonOffice);
        kitchenButton = (Button) view.findViewById(R.id.buttonKitchen);
        foodButton = (Button) view.findViewById(R.id.buttonFood);
        petItemsButton = (Button) view.findViewById(R.id.buttonPetItems);
        bcGamesButton = (Button) view.findViewById(R.id.buttonBCGames);
        drinksButton = (Button) view.findViewById(R.id.buttonDrinks);
        sidesButton = (Button) view.findViewById(R.id.buttonSides);
        dessertsButton = (Button) view.findViewById(R.id.buttonDesserts);
        roadTripButton = (Button) view.findViewById(R.id.buttonRoadTrip);
        officeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("Office: ", "Office button clicked");
                onClickPlay();
            }
        });
        kitchenButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("Kitchen: ", "Kitchen button clicked");
                onClickPlay();
            }
        });
        foodButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("Food: ", "Food button clicked");
                onClickPlay();
            }
        });
        petItemsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("Pet Items: ", "Pet Items button clicked");
                onClickPlay();
            }
        });
        bcGamesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("Board & Card Games: ", "Games button clicked");
                onClickPlay();
            }
        });
        drinksButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("Drinks: ", "Drinks button clicked");
                onClickPlay();
            }
        });
        sidesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("Sides: ", "Sides button clicked");
                onClickPlay();
            }
        });
        dessertsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("Desserts: ", "Desserts button clicked");
                onClickPlay();
            }
        });
        roadTripButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("Road Trip: ", "Road Trip button clicked");
                onClickPlay();
            }
        });
        Log.d("Topics: ", "it should work");
        return view;
    }

    private void onClickPlay(){
        Log.d("Button: ", "onclickplay");

        FragmentActivity activity = this.getActivity();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, new fragment_Versus(), "Versus")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
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
        Log.d("Topics: ", "Attach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
