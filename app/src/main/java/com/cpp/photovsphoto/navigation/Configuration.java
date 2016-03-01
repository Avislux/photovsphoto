package com.cpp.photovsphoto.navigation;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.cpp.photovsphoto.AnalyzeActivity;
import com.cpp.photovsphoto.R;
import com.cpp.photovsphoto.demo.IdentityDemoFragment;
import com.cpp.photovsphoto.fragments.fragment_analyze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jonathan on 2/24/2016.
 */
public class Configuration {

    private static final List<Feature> features = new ArrayList<Feature>(); //fills navigation drawer and home screen
    static{
        addFeature("analyze", R.mipmap.user_identity, R.string.text_Analyze_title, //TODO: change mipmap images
                 R.string.text_Analyze_overview,
                new navItem(R.string.text_Analyze_title, R.mipmap.user_identity,
                        R.string.text_Analyze_title, fragment_analyze.class));
    }

    public static List<Feature> getFeatureList() {
        return Collections.unmodifiableList(features);
    }
    public static Feature getFeatureByName(final String name) {
        for (Feature feature : features) {
            if (feature.name.equals(name)) {
                return feature;
            }
        }
        return null;
    }
    private static void addFeature(final String name, final int iconResId, final int titleResId,

                                   final int descriptionResId,
                                   final navItem... demoItems) {
        Feature feature = new Feature(name, iconResId, titleResId,
                descriptionResId,  demoItems);
        features.add(feature);
    }
    public static class Feature{
        public String name;
        public int iconResId;
        public int titleResId;
        public int subtitleResId;
        public int descriptionResId;
        public List<navItem> listItems;
        public Feature(){} //this is a blank declaration for home "feature"

        public Feature(final String name, final int iconResId, final int titleResId,
                       final int descriptionResId,
                       final navItem... menuItems) { // ... is arbitrary number of arguments
            this.name = name;
            this.iconResId = iconResId;
            this.titleResId = titleResId;
            this.descriptionResId = descriptionResId;
            this.listItems = Arrays.asList(menuItems);
        }
    }
    public static class navItem {
        public int titleResId;
        public int iconResId;
        public int buttonTextResId;
        public String fragmentClassName;
//final Class<? extends Fragment> fragmentClass
        public navItem(final int titleResId, final int iconResId, final int buttonTextResId,
                        final Class <? extends Fragment> fragmentClass) {
            this.titleResId = titleResId;
            this.iconResId = iconResId;
            this.buttonTextResId = buttonTextResId;
            this.fragmentClassName = fragmentClass.getName();
        }
    }
}
