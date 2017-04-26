package com.jose.foundies;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jason on 4/19/2017.
 */

public class ProfileTab4 extends Fragment{
    private EditText msg_edittext;
    private String user1 = "khushi", user2 = "khushi1";
    private Random random;
    public static ArrayList<ChatMessage> chatlist;
    public static ChatAdapter chatAdapter;
    ListView msgListView;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab4_chats, container, false);
        final Controller controller = (Controller) getActivity().getApplicationContext();

        final Button message = (Button) rootView.findViewById(R.id.message_button);
        final Button chatRoom = (Button) rootView.findViewById(R.id.chatRoom_button);
        final Button view = (Button) rootView.findViewById(R.id.message_button);
        final ListView notificationList = (ListView) rootView.findViewById(R.id.notifyList);

        //Fill list view with strings
        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(controller.getNameOfUser() + " wants to contact you!");
        testArray.add(controller.getNameOfUser() + " sent you a message!");
        ArrayAdapter<String> adapter = new ArrayAdapter(ProfileTab4.this.getContext(), R.layout.list_notifications,R.id.txt, testArray);
        notificationList.setAdapter(adapter);

        //clickable listview
        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Create Dialog
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileTab4.this.getContext());
                View mView = inflater.inflate(R.layout.dialog_notification, null);
                TextView firstName = (TextView) mView.findViewById(R.id.userFirst_tv);
                firstName.setText("Do you want to accept " + controller.getNameOfUser() + "'s invitation?");
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        //pop up message dialog
        message.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //Create Dialog
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileTab4.this.getContext());
                View mView = inflater.inflate(R.layout.dialog_message, null);
                TextView firstName = (TextView) mView.findViewById(R.id.userFirst_tv);
                firstName.setText("Send " + controller.getNameOfUser().toString() + " a message");
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });
        chatRoom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                controller.setQueryTypeFound();
                Intent i = new Intent(ProfileTab4.this.getContext(), Chats.class);
                startActivity(i);
                getActivity().finish();

            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //Create Dialog
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileTab4.this.getContext());
                View mView = inflater.inflate(R.layout.dialog_message, null);
                TextView firstName = (TextView) mView.findViewById(R.id.userFirst_tv);
                firstName.setText("Send " + controller.getNameOfUser().toString() + " a message");
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });

        TextView welcomeText = (TextView) rootView.findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome " + controller.getNameOfUser() + "!");
        return rootView;
    }





}