package com.jose.foundies;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by Jason on 4/10/2017.
 */

public class ItemDialog extends DialogFragment {


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;

    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Was the item lost at " + address + "?");

        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflate.inflate(R.layout.dialog_box, null);
        builder.setView(dialogView);

        Button yesButton = (Button) dialogView.findViewById(R.id.dialogButtonYes);
        Button noButton = (Button) dialogView.findViewById(R.id.dialogButtonNo);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ItemDialog.this.getContext(), LostConfirmation.class);
                startActivity(i);
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemDialog.this.getDialog().cancel();
            }
        });
        
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
