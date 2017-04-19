package com.jose.foundies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jason on 4/19/2017.
 */

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
                startActivity(i);
                getActivity().finish();
            }
        });

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        Map<String,String> datum2;
        if(foundItems !=null) {
            for (int i = 0; i < foundItems.size(); ++i) {
                System.out.println("SIZE: " + foundItems.size());
                datum2 = new HashMap<String, String>(2);
                datum2.put("Question", foundItems.get(i).getCategory());
                datum2.put("Answer", foundItems.get(i).getSubcategory());
                data.add(datum2);
            }

            SimpleAdapter listAdapter2 = new SimpleAdapter(this.getContext(), data, android.R.layout.simple_list_item_2,
                    new String[]{"Question", "Answer"},
                    new int[]{android.R.id.text1,
                            android.R.id.text2});

            ListView confirmList = (ListView) rootView.findViewById(R.id.foundList);
            confirmList.setAdapter(listAdapter2);
        }else{
            System.out.println("NOTHING WAS BROUGHT IN");
        }
        return rootView;
    }
}