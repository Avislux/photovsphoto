package com.cpp.photovsphoto.navigation;

import android.support.v4.app.Fragment;

import com.cpp.photovsphoto.R;
import com.cpp.photovsphoto.demo.IdentityDemoFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jonathan on 2/24/2016.
 */
public class Configuration {

    private static final List<Feature> features = new ArrayList<Feature>(); //fills navigation drawer
    static{
        addFeature("analyze", R.mipmap.user_identity, R.string.text_Analyze_title, //change mipmap images
                R.string.text_Analyze_overview, R.string.text_Analyze_overview,
                new navItem(R.string.text_Analyze_title, R.mipmap.user_identity,
                        R.string.text_Analyze_title, IdentityDemoFragment.class));
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
                                   final int subtitleResId,
                                   final int descriptionResId,
                                   final navItem... demoItems) {
        Feature feature = new Feature(name, iconResId, titleResId, subtitleResId,
                descriptionResId,  demoItems);
        features.add(feature);
    }
    public static class Feature{
        public String name;
        public int iconResId;
        public int titleResId;
        public int subtitleResId;
        public int descriptionResId;
        public List<navItem> demos;
        public Feature() {

        }

        public Feature(final String name, final int iconResId, final int titleResId,
                       final int subtitleResId,
                       final int descriptionResId,
                       final navItem... demoItems) {
            this.name = name;
            this.iconResId = iconResId;
            this.titleResId = titleResId;
            this.subtitleResId = subtitleResId;

            this.descriptionResId = descriptionResId;

            this.demos = Arrays.asList(demoItems);
        }
    }
    public static class navItem {
        public int titleResId;
        public int iconResId;
        public int buttonTextResId;
        public String fragmentClassName;

        public navItem(final int titleResId, final int iconResId, final int buttonTextResId,
                        final Class<? extends Fragment> fragmentClass) {
            this.titleResId = titleResId;
            this.iconResId = iconResId;
            this.buttonTextResId = buttonTextResId;
            this.fragmentClassName = fragmentClass.getName();
        }
    }
}
