package com.example.cmrussell.dbdummy;

/**
 * Created by cmrussell on 3/2/17.
 */

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HerokuService {
    @GET("/hello")
    Call<ResponseBody> hello();

    @GET("/users")
    Call<ResponseBody> getUsers();
}
