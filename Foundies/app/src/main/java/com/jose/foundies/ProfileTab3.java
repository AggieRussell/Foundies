package com.jose.foundies;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jose.foundies.R.drawable.small_back;

/**
 * Created by Jason on 4/19/2017.
 */
class CustomList2 extends BaseAdapter {
    private ArrayList<Item> items = new ArrayList<>();
    private Context context;

    public CustomList2(Context context, ArrayList<Item> items){
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount(){
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_with_button2, null);
        final int index = i;
        final Controller controller = (Controller) context.getApplicationContext();

        TextView txtListChild = (TextView) view.findViewById(R.id.listInfo);
        txtListChild.setText(items.get(i).getCategory());

        TextView txtListChild2 = (TextView) view.findViewById(R.id.listInfo2);
        txtListChild2.setText(items.get(i).getSubcategory());

        Button listButton = (Button) view.findViewById(R.id.listButton);
        Button deleteButton = (Button) view.findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflate.inflate(R.layout.dialog_box, null);
            builder.setView(dialogView);
            // Create the AlertDialog
            final AlertDialog dialog = builder.create();

            dialog.setMessage("Are you sure you want to delete this item?");

            Button yesButton = (Button) dialogView.findViewById(R.id.dialogButtonYes);
            Button noButton = (Button) dialogView.findViewById(R.id.dialogButtonNo);

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    controller.deleteFoundItem(items.get(index));
                    items.remove(index);
                    notifyDataSetChanged();
                    dialog.cancel();
                }
            });
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            dialog.show();
            }
        });
        //listButton.setBackground(ContextCompat.getDrawable(context, R.drawable.small_back));
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            controller.setCurrentItem(items.get(index));
            controller.setQueryTypeLost();
            Intent i = new Intent(context, FoundMap.class);
            context.startActivity(i);
            ((Activity)context).finish();
            Toast toast = Toast.makeText(context, "Sending to Map...", Toast.LENGTH_SHORT);
            toast.show();
            }
        });
        return view;
    }
}
public class ProfileTab3 extends Fragment{

    public ArrayList<Item> getLostItems() {
        return lostItems;
    }

    public void setLostItems(ArrayList<Item> lostItems) {
        this.lostItems = lostItems;
    }

    private ArrayList<Item> lostItems;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_lost_items, container, false);

        final Controller controller = (Controller) getActivity().getApplicationContext();

        final Button lost = (Button) rootView.findViewById(R.id.lost_button);
        lost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            controller.setQueryTypeLost();
            Intent i = new Intent(ProfileTab3.this.getContext(), Qs.class);
/*
            if(!controller.checkQueryLostCount()){
                Toast toast = Toast.makeText(getActivity(), "OVER THE QUERY LIMIT", Toast.LENGTH_SHORT);
                toast.show();
            }else {
                startActivity(i);
                getActivity().finish();
            }
*/
            startActivity(i);
            getActivity().finish();
            }
        });

        if(lostItems != null) {
            ListView confirmList = (ListView) rootView.findViewById(R.id.lostList);
            CustomList2 adapter = new CustomList2(this.getContext(), lostItems);
            confirmList.setAdapter(adapter);
        }
        return rootView;
    }
}