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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jason on 4/19/2017.
 */

class CustomList extends BaseAdapter {
    private ArrayList<Item> items = new ArrayList<>();
    private Context context;

    public CustomList(Context context, ArrayList<Item> items){
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
        view = inflater.inflate(R.layout.list_with_button, null);
        final Controller controller = (Controller) context.getApplicationContext();

        TextView txtListChild = (TextView) view.findViewById(R.id.listInfo);
        txtListChild.setText(items.get(i).getCategory());

        TextView txtListChild2 = (TextView) view.findViewById(R.id.listInfo2);
        txtListChild2.setText(items.get(i).getSubcategory());
        final Item item = items.get(i);
        final int index = i;
        Button listButton = (Button) view.findViewById(R.id.listButton);
        listButton.setOnClickListener(new View.OnClickListener() {
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
                        controller.deleteFoundItem(item);
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
        return view;
    }
}

public class ProfileTab2 extends Fragment{

    private ArrayList<Item> foundItems;

    public ArrayList<Item> getFoundItems() {
        return foundItems;
    }

    public void setFoundItems(ArrayList<Item> foundItems) {
        this.foundItems = foundItems;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_found_items, container, false);

        final Controller controller = (Controller) getActivity().getApplicationContext();

        final Button found = (Button) rootView.findViewById(R.id.found_button);
        found.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                controller.setQueryTypeFound();
                Intent i = new Intent(ProfileTab2.this.getContext(), Qs.class);
                if(!controller.checkQueryFoundCount()){
                    Toast toast = Toast.makeText(getActivity(), "OVER THE QUERY LIMIT", Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    startActivity(i);
                    getActivity().finish();
                }
            }
        });

        if(foundItems != null) {
            ListView confirmList = (ListView) rootView.findViewById(R.id.foundList);
            CustomList adapter = new CustomList(this.getContext(), foundItems);
            confirmList.setAdapter(adapter);
        }
        //confirmList.setAdapter(listAdapter2);
        return rootView;
    }
}