package com.example.cmrussell.dbdummy;

/**
 * Created by cmrussell on 3/2/17.
 */

import java.util.Observable;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HerokuService {
    @GET("/hello")
    Call<ResponseBody> hello();

    @GET("/users")
    Call<ResponseBody> getUsers();

    //FormUrlEncoded
    @POST("/users")
    Call<ResponseBody> createUser(@Body RequestBody user);

    @DELETE("/user/{id}")
    Call<ResponseBody> deleteUser(@Path("id") String username);
}
