package com.jose.foundies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

//this is the data structure used to create the expandable list
class CustomExpandableList extends BaseExpandableListAdapter {

    private Context context;
    private List<FoundItem> listHeader;
    private HashMap<FoundItem, List<String>> listChild;

    public CustomExpandableList(Context cntx, List<FoundItem> header, HashMap<FoundItem, List<String>> child){
        context = cntx;
        listHeader = header;
        listChild = child;
    }

    @Override
    public int getGroupCount() {
        return listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listChild.get(listHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listChild.get(listHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        FoundItem header = (FoundItem) getGroup(groupPosition);
        String headerTitle = header.getTimestamp();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_items, null);
        }

        TextView listInformation = (TextView) convertView.findViewById(R.id.listItems);
        listInformation.setTypeface(null, Typeface.BOLD);
        listInformation.setText("Found Item");
        listInformation.setTextColor(Color.WHITE);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_information, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.listInformation);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}


public class LostConfirmation extends AppCompatActivity {

    private CustomExpandableList adapter;
    private ExpandableListView expList;
    private HashMap<FoundItem, List<String>> itemInfo = new HashMap<FoundItem, List<String>>();
    private List<FoundItem> items = new ArrayList<>();
    private Controller controller = null;
    private List<FoundItem> unfilteredItems;
    private static LatLng location;
    private FoundItem itemSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        controller = (Controller) getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_confirmation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        unfilteredItems = controller.getFoundItems();

        expList = (ExpandableListView) findViewById(R.id.matchResponses);
        adapter = new CustomExpandableList(this, items, itemInfo);
        expList.setAdapter(adapter);

        expList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                itemSelected = items.get(i);
                Toast toast = Toast.makeText(getApplicationContext(), "Item User: " + items.get(i).getUser(), Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }
        });

        Button nextButton = (Button) findViewById(R.id.nextButton);
        Button backButton = (Button) findViewById(R.id.backButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemSelected != null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "TODO: Contact User", Toast.LENGTH_SHORT);
                    toast.show();
                    /* Create the Intent */
                    Intent email = new Intent(android.content.Intent.ACTION_SEND);

                    /* Fill it with Data */
                    email.setType("plain/text");
                    email.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{itemSelected.getUser()});
                    email.putExtra(android.content.Intent.EXTRA_SUBJECT, "Foundies item match!");

                    /* Send it off to the Activity-Chooser */
                    startActivity(Intent.createChooser(email, "Send email"));
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Select an item", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), FoundMap.class);
                startActivity(i);
                finish();
            }
        });

        for(int i = 0; i < unfilteredItems.size(); i++){
            if(Double.parseDouble(unfilteredItems.get(i).getLat()) == controller.getLatitude() &&
                    Double.parseDouble(unfilteredItems.get(i).getLng()) == controller.getLongitude()){
                items.add(unfilteredItems.get(i));
            }
        }
        makeList();
    }

    //prepare the list to put into the expListView
    //This is where we will use the values from the database
    private void makeList()
    {
        for(int i = 0; i < items.size(); ++i){
            List<String> itemList = new ArrayList<>();
            itemList.add(items.get(i).getCategory1());
            itemList.add(items.get(i).getCategory2());
            itemList.add(items.get(i).getCategory3());
            itemList.add(items.get(i).getTimestamp());
            itemInfo.put(items.get(i), itemList);
        }
    }
}
