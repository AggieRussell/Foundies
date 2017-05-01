package com.jose.foundies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoadScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);
        final Controller controller = (Controller) getApplicationContext();

        final Button join = (Button) findViewById(R.id.join_button);
        final Button signin = (Button) findViewById(R.id.signin_tv);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflate = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflate.inflate(R.layout.dialog_box, null);
        builder.setView(dialogView);
        // Create the AlertDialog
        final AlertDialog dialog = builder.create();

        dialog.setMessage("Foundies would like to use your location.");

        Button yesButton = (Button) dialogView.findViewById(R.id.dialogButtonYes);
        Button noButton = (Button) dialogView.findViewById(R.id.dialogButtonNo);
        yesButton.setText("Allow");
        noButton.setText("Deny");

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.setLocationEnabled(true);
                dialog.cancel();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.setLocationEnabled(false);
                dialog.cancel();
            }
        });
        dialog.show();

        join.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), Register.class);
                startActivity(i);
                finish();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), Login.class);
                System.out.println("KYLEWTF: " + controller.isLocationEnabled());
                startActivity(i);
                finish();
            }
        });
    }
}
