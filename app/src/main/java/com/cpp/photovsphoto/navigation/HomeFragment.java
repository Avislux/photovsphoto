package com.cpp.photovsphoto.navigation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cpp.photovsphoto.R;
import com.cpp.photovsphoto.fragments.fragment_PlayOnline;
import com.cpp.photovsphoto.fragments.fragment_PlaySolo;
import com.cpp.photovsphoto.fragments.fragment_analyze;
import com.cpp.photovsphoto.navigation.Configuration;
import com.cpp.photovsphoto.navigation.FragmentBase;
//import com.cpp.photovsphoto.navigation.InstructionFragment;

public class HomeFragment extends FragmentBase {
    private String logTag = "HomeMenu: ";
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false); //opens home_fragment
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListAdapter adapter = new ListAdapter(getActivity());
        adapter.addAll(Configuration.getFeatureList());

        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view,
                                    final int position, final long id) {
                final Configuration.Feature item = adapter.getItem(position);
                final AppCompatActivity activity = (AppCompatActivity) getActivity();
                if (activity != null) {

                    //  final Fragment fragment = InstructionFragment.newInstance(item.name);
                    final Fragment fragment = new fragment_analyze();
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment_container, fragment, item.name)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();

                    // Set the title for the fragment.
                    final ActionBar actionBar = activity.getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setTitle(item.titleResId);
                    }
                }

                switch(position) {


                    case 0:
                        Log.d(logTag, "Case 1"); //playsolo
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_fragment_container, new fragment_PlaySolo(), item.name)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();

                    case 1:
                        Log.d(logTag, "Case 2"); //playonline
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_fragment_container, new fragment_PlayOnline(), item.name)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();
                        break;/**/
                    case 2:
                        Log.d(logTag, "Case 3");//analyze
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_fragment_container, new fragment_analyze(), item.name)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();
                        break;
                }

            }
        });
    }

    private static final class ListAdapter extends ArrayAdapter<Configuration.Feature> {
        private LayoutInflater inflater;

        public ListAdapter(final Context context) {
            super(context, R.layout.list_item_icon_text_with_subtitle);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view;
            ViewHolder holder;
            if (convertView == null) {
                view = inflater.inflate(R.layout.list_item_icon_text_with_subtitle, parent, false);
                holder = new ViewHolder();
                holder.iconImageView = (ImageView) view.findViewById(R.id.list_item_icon);
                holder.titleTextView = (TextView) view.findViewById(R.id.list_item_title);
               // holder.subtitleTextView = (TextView) view.findViewById(R.id.list_item_subtitle);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) convertView.getTag();
            }

            Configuration.Feature item = getItem(position);
            holder.iconImageView.setImageResource(item.iconResId);
            holder.titleTextView.setText(item.titleResId);


            return view;
        }
    }

    private static final class ViewHolder {
        ImageView iconImageView;
        TextView titleTextView;
        TextView subtitleTextView;
    }
}
