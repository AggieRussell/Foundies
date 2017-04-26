package com.jose.foundies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//this is the data structure used to create the expandable list
class CustomExpandableList extends BaseExpandableListAdapter {

    private Context context;
    private List<Item> listHeader;
    private HashMap<Item, List<String>> listChild;

    public CustomExpandableList(Context cntx, List<Item> header, HashMap<Item, List<String>> child){
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
        Item header = (Item) getGroup(groupPosition);
        String headerTitle = header.getTimestamp();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_items, null);
        }

        TextView listInformation = (TextView) convertView.findViewById(R.id.listItems);
        listInformation.setTypeface(null, Typeface.BOLD);
        listInformation.setText("Item " + (groupPosition+1));
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
    private HashMap<Item, List<String>> itemInfo = new HashMap<Item, List<String>>();
    private List<Item> items = new ArrayList<>();
    private Controller controller = null;
    private List<Item> unfilteredItems;
    private static LatLng location;
    private Item itemSelected;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            Intent i = new Intent(getBaseContext(), ProfilePage.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        controller = (Controller) getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_confirmation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            unfilteredItems = controller.getFoundItems();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        expList = (ExpandableListView) findViewById(R.id.matchResponses);
        adapter = new CustomExpandableList(this, items, itemInfo);
        final TextView selectedItem = (TextView) findViewById(R.id.selectedItem);
        expList.setAdapter(adapter);



        expList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                itemSelected = items.get(i);
                selectedItem.setText("Selected Item -  Item " + (i+1));
            }
        });
        expList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                itemSelected = null;
                selectedItem.setText("Selected Item -");
            }
        });

        Button nextButton = (Button) findViewById(R.id.nextButton);
        Button backButton = (Button) findViewById(R.id.backButton);
        Button postButton = (Button) findViewById(R.id.postButton);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LostConfirmation.this);

                // Create the AlertDialog
                final AlertDialog dialog = builder.create();

                dialog.setMessage("Post new lost item?");

                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        controller.postLostItem();
                        Toast toast = Toast.makeText(getApplicationContext(), "POSTED TO LOST DATABASE", Toast.LENGTH_SHORT);
                        controller.setLatLong(0.0, 0.0);
                        Intent lostOrFound = new Intent(getBaseContext(), ProfilePage.class);
                        startActivity(lostOrFound);
                        finish();
                        toast.show();
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

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(itemSelected != null) {
                /* Create the Intent */
                AlertDialog.Builder builder = new AlertDialog.Builder(LostConfirmation.this);

                // Create the AlertDialog
                final AlertDialog dialog = builder.create();

                dialog.setMessage("Send an email to the finder of the item?");

                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    Intent email = new Intent(android.content.Intent.ACTION_SEND);
                    /* Fill it with Data */
                    email.setType("plain/text");
                    email.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{itemSelected.getUserID()});
                    email.putExtra(android.content.Intent.EXTRA_SUBJECT, "Foundies item match!");

                    startActivityForResult(Intent.createChooser(email, "Send email"), 1);
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
            if(unfilteredItems.get(i).getLatitude() == controller.getLatitude() &&
                    unfilteredItems.get(i).getLongitude() == controller.getLongitude()){
                items.add(unfilteredItems.get(i));
            }
        }
        if(items.size() == 0){
            selectedItem.setText("No items at this location!");
            nextButton.setClickable(false);
            nextButton.setBackgroundColor(Color.parseColor("#BBBBBB"));
            nextButton.setTextColor(Color.GRAY);
        }
        makeList();
    }

    //prepare the list to put into the expListView
    //This is where we will use the values from the database
    private void makeList()
    {
        for(int i = 0; i < items.size(); ++i){
            List<String> itemList = new ArrayList<>();
            for(int j = 0; j < items.get(i).getAnswers().size(); j++){
                itemList.add(controller.getNames().get(j) + ": " + items.get(i).getAnswers().get(j));
            }
            itemList.add(items.get(i).getTimestamp());
            itemInfo.put(items.get(i), itemList);
        }
    }
}
