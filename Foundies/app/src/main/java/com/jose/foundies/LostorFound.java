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
import android.widget.TextView;

import javax.microedition.khronos.opengles.GL10;

public class LostorFound extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_or_found);

        final Controller controller = (Controller) getApplicationContext();

        final Button found = (Button) findViewById(R.id.found_button);
        final Button lost = (Button) findViewById(R.id.lost_button);

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
