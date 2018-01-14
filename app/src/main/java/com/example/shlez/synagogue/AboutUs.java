package com.example.shlez.synagogue;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by Shlez on 11/25/17.
 */

public class AboutUs extends Fragment {


    private static final String TAG = "AboutUs";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("About");

        Element titleElement = new Element();
        titleElement.setTitle("About Synagogue");
        Element versionElement = new Element();
        versionElement.setTitle("Version 1.0");


        View aboutPage = new AboutPage(getActivity())
                .isRTL(false)
                .setDescription(getString(R.string.about_description))
                .setImage(R.drawable.synagogue_small)
                .addEmail("shlez1993.hs@gmail.com")
                .addItem(versionElement)
                .create();

        return aboutPage;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
