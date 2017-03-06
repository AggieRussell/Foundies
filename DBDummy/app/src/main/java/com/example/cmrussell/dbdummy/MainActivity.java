package com.example.cmrussell.dbdummy;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity {

    String response_str = "";
    JSONObject jObject;
    JSONArray jArray;
    TextView greeting;
    ArrayList<ArrayList<String>> dbResults = new ArrayList<ArrayList<String>>();
    ArrayList<TextView> ids = new ArrayList<>();
    ArrayList<TextView> firstnames = new ArrayList<>();
    ArrayList<TextView> lastnames = new ArrayList<>();
    ArrayList<TextView> usernames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        out.println("Starting onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        greeting = (TextView) findViewById(R.id.greeting);

        loadViewContent();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pacific-tor-50594.herokuapp.com")
                .build();

        final HerokuService service = retrofit.create(HerokuService.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> call = service.getUsers();
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> _,
                                           Response<ResponseBody> response) {
                        try {
                            response_str = response.body().string();
                            greeting.setText("Here are the current users:");
                            // out.println(response_str);
                            parseJSON();
                        } catch (IOException e) {
                            e.printStackTrace();
                            greeting.setText(e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> _, Throwable t) {
                        t.printStackTrace();
                        greeting.setText(t.getMessage());
                    }
                });
            }
        });
    }

    public void loadViewContent() {
        out.println("Starting load content...");
        ids.clear();
        firstnames.clear();
        lastnames.clear();
        usernames.clear();
        ids.add((TextView) findViewById(R.id.id1));
        ids.add((TextView) findViewById(R.id.id2));
        ids.add((TextView) findViewById(R.id.id3));
        ids.add((TextView) findViewById(R.id.id4));
        ids.add((TextView) findViewById(R.id.id5));
        firstnames.add((TextView) findViewById(R.id.firstname1));
        firstnames.add((TextView) findViewById(R.id.firstname2));
        firstnames.add((TextView) findViewById(R.id.firstname3));
        firstnames.add((TextView) findViewById(R.id.firstname4));
        firstnames.add((TextView) findViewById(R.id.firstname5));
        lastnames.add((TextView) findViewById(R.id.lastname1));
        lastnames.add((TextView) findViewById(R.id.lastname2));
        lastnames.add((TextView) findViewById(R.id.lastname3));
        lastnames.add((TextView) findViewById(R.id.lastname4));
        lastnames.add((TextView) findViewById(R.id.lastname5));
        usernames.add((TextView) findViewById(R.id.username1));
        usernames.add((TextView) findViewById(R.id.username2));
        usernames.add((TextView) findViewById(R.id.username3));
        usernames.add((TextView) findViewById(R.id.username4));
        usernames.add((TextView) findViewById(R.id.username5));
        for (int i=0; i<ids.size(); ++i) {
            ids.get(i).setVisibility(View.GONE);
            firstnames.get(i).setVisibility(View.GONE);
            lastnames.get(i).setVisibility(View.GONE);
            usernames.get(i).setVisibility(View.GONE);
        }
        out.println("Finished load content...");
    }

    public void updateDisplay() {
        out.println("Starting update display...");
        for(int i=0; i<dbResults.size(); ++i) {
            ArrayList<String> curr = dbResults.get(i);
            int j=0;
            if (curr.get(j).charAt(0)!='{') {
                ids.get(i).setText(curr.get(j));
                ids.get(i).setVisibility(View.VISIBLE);
            }
            else {
                ids.get(i).setText("--");
                ids.get(i).setVisibility(View.VISIBLE);
            }
            ++j;
            firstnames.get(i).setText(curr.get(j));
            firstnames.get(i).setVisibility(View.VISIBLE);
            ++j;
            lastnames.get(i).setText(curr.get(j));
            lastnames.get(i).setVisibility(View.VISIBLE);
            ++j;
            usernames.get(i).setText(curr.get(j));
            usernames.get(i).setVisibility(View.VISIBLE);
        }
        out.println("Ending update display...");
    }

    public void parseJSON() {
        out.println("Starting parse JSON...");
        try {
            jObject = new JSONObject(response_str);
            jArray = jObject.getJSONArray("users");
            for(int i=0; i<jArray.length(); ++i) {
                out.println(jArray.getString(i));
                ArrayList<String> newUser = new ArrayList<String>();
                out.println(jArray.getString(i));
                JSONObject curr = new JSONObject(jArray.getString(i));
                newUser.add(curr.getString("_id"));
                newUser.add(curr.getString("first_name"));
                newUser.add(curr.getString("last_name"));
                newUser.add(curr.getString("username"));
                dbResults.add(newUser);
                updateDisplay();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            greeting.setText(e.getMessage());
        }
        out.println("Ending parse JSON...");
    }

    //    greeting.setText("booyah!");

    //    MongoClientURI uri = new MongoClientURI("mongodb://ds157549.mlab.com:57549/foundiesdb");
    //    MongoClient client = new MongoClient(uri);
    //    MongoDatabase db = client.getDatabase(uri.getDatabase());
    //    boolean auth = db.authenticate("kyle.preston75", "shell2017".toCharArray());

    //    if (auth) {
    //        System.out.println("Successfully logged in to MongoDB!");
    //    } else {
    //        System.out.println("Invalid username/password");
    //    }
        // mongodb://kyle.preston75:shell2017@ds157549.mlab.com:57549/foundiesdb
        //

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
