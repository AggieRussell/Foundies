package com.jose.foundies;

import android.view.View;
import static java.lang.System.out;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by christine on 3/27/17.
 */

public class LostModel {

    private String response_str;

    public ArrayList<String> getQuestions() {
        out.println("Made it to lost model");
        final ArrayList<String> q = new ArrayList<String>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pacific-tor-50594.herokuapp.com")
                .build();

        final HerokuService service = retrofit.create(HerokuService.class);

        Call<ResponseBody> call = service.getQuestions();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> _,
                                   Response<ResponseBody> response) {
                try {
                    response_str = response.body().string();
                    out.println(response_str);
                    q.add(response_str);
                    //return parseJSON();
                } catch (IOException e) {
                    e.printStackTrace();
                    q.add(e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> _, Throwable t) {
                t.printStackTrace();
                q.add(t.toString());
            }
        });

        return q;
    }

}
