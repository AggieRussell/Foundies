package com.jose.foundies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.doubleclick.CustomRenderedAd;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jason on 4/19/2017.
 */

class CustomList4 extends BaseAdapter {
    private ArrayList<ChatMessage> msgs = new ArrayList<>();
    private Context context;

    public CustomList4(Context context, ArrayList<ChatMessage> msgs){
        this.msgs = msgs;
        this.context = context;
    }

    @Override
    public int getCount(){
        return msgs.size();
    }

    @Override
    public Object getItem(int i) {
        return msgs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_with_button, null);
        final Controller controller = (Controller) context.getApplicationContext();

        TextView txtListChild = (TextView) view.findViewById(R.id.listInfo);
        txtListChild.setText(msgs.get(i).getNotificationType());

        TextView txtListChild2 = (TextView) view.findViewById(R.id.listInfo2);
        txtListChild2.setText(msgs.get(i).getBody());
        final ChatMessage message = msgs.get(i);
        final int index = i;
        Button listButton = (Button) view.findViewById(R.id.listButton);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                // Create the AlertDialog
                final AlertDialog dialog = builder.create();

                dialog.setMessage("Are you sure you want to delete this item?");

                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        controller.deleteMessage(message);
                        msgs.remove(index);
                        notifyDataSetChanged();
                        dialog.cancel();
                    }
                });

                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
        return view;
    }
}

public class ProfileTab4 extends Fragment{

    private ArrayList<ChatMessage> messages;

    public ArrayList<ChatMessage> getAllMsgs() {
        return messages;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab4_chats, container, false);

        final Controller controller = (Controller) getActivity().getApplicationContext();

        final Button found = (Button) rootView.findViewById(R.id.found_button);
        found.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                controller.setQueryTypeFound();
                Intent i = new Intent(ProfileTab4.this.getContext(), Qs.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        if(messages != null) {
            ListView confirmList = (ListView) rootView.findViewById(R.id.foundList);

            CustomList4 adapter = null;
            confirmList.setAdapter(adapter);
        }
        //confirmList.setAdapter(listAdapter2);
        return rootView;
    }
}