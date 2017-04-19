package com.jose.foundies;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

/**
 * Created by Jason on 4/19/2017.
 */

public class ProfileTab1 extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_notifications, container, false);
        final Controller controller = (Controller) getActivity().getApplicationContext();

        TextView welcomeText = (TextView) rootView.findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome " + controller.getNameOfUser() + "!");
        return rootView;
    }
}