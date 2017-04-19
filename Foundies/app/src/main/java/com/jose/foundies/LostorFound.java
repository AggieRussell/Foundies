package com.jose.foundies;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.EGL14;
import android.opengl.EGLDisplay;
import android.opengl.GLES10;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;

import javax.microedition.khronos.opengles.GL10;

public class LostorFound extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_or_found);

        final Controller controller = (Controller) getApplicationContext();

        final Button found = (Button) findViewById(R.id.found_button);
        final Button lost = (Button) findViewById(R.id.lost_button);

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        Map<String,String> datum;
        if(controller.getUsersLostItems() !=null) {
            for (int i = 0; i < controller.getUsersLostItems().size(); ++i) {
                System.out.println("SIZE: " + controller.getUsersLostItems().size());
                datum = new HashMap<String, String>(2);
                datum.put("Question", controller.getUsersLostItems().get(i).getCategory());
                datum.put("Answer", controller.getUsersLostItems().get(i).getSubcategory());
                data.add(datum);
            }
            
            SimpleAdapter listAdapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2,
                    new String[]{"Question", "Answer"},
                    new int[]{android.R.id.text1,
                            android.R.id.text2});

            ListView confirmList = (ListView) findViewById(R.id.lostList);
            confirmList.setAdapter(listAdapter);
        }else{
            System.out.println("NOTHING WAS BROUGHT IN");
        }

        found.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                controller.setQueryTypeFound();
                Intent i = new Intent(getBaseContext(), Qs.class);
                startActivity(i);
                finish();
            }
        });
        lost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                controller.setQueryTypeLost();
                Intent i = new Intent(getBaseContext(), Qs.class);
                startActivity(i);
                finish();
            }
        });
        TextView welcomeText = (TextView) findViewById(R.id.textView6);
        welcomeText.setText("Welcome " + controller.getNameOfUser() + "!");
    }
}
