package com.jose.foundies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jason on 4/19/2017.
 */

public class ProfileTab3 extends Fragment{
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
                if(!controller.checkQueryLostCount()){
                    Toast toast = Toast.makeText(getActivity(), "OVER THE QUERY LIMIT", Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    startActivity(i);
                    getActivity().finish();
                }
            }
        });

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        Map<String,String> datum2;
        if(controller.getUsersLostItems() !=null) {
            for (int i = 0; i < controller.getUsersLostItems().size(); ++i) {
                System.out.println("SIZE: " + controller.getUsersLostItems().size());
                datum2 = new HashMap<String, String>(2);
                datum2.put("Question", controller.getUsersLostItems().get(i).getCategory());
                datum2.put("Answer", controller.getUsersLostItems().get(i).getSubcategory());
                data.add(datum2);
            }

            SimpleAdapter listAdapter2 = new SimpleAdapter(this.getContext(), data, android.R.layout.simple_list_item_2,
                    new String[]{"Question", "Answer"},
                    new int[]{android.R.id.text1,
                            android.R.id.text2});

            ListView confirmList = (ListView) rootView.findViewById(R.id.lostList);
            confirmList.setAdapter(listAdapter2);
        }else{
            System.out.println("NOTHING WAS BROUGHT IN");
        }
        return rootView;
    }
}