package com.jose.foundies;

/**
 * Created by kylepreston on 3/27/17.
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
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HerokuService {
    @GET("/hello")
    Call<ResponseBody> hello();

    @GET("/users")
    Call<ResponseBody> getUsers();

    @GET("/questions")
    Call<ResponseBody> getQuestions();

    @GET("/questions/qs?")
    Call<ResponseBody> getQuestionsWithQs(@Query("q1") String q1, @Query("q2") String q2);

    @GET("/items/found/bycategories?")
    Call<ResponseBody> getFoundItemWithCategories(@Query("cc") String q1, @Query("sc") String q2);

    @GET("/user/{username}")
    Call<ResponseBody> getUsersByUsername(@Path("username") String username);

    @GET("/items/found")
    Call<ResponseBody> getFoundItems();

    @GET("/items/found/{username}")
    Call<ResponseBody> getFoundItemsByUser(@Path("username") String username);

    @GET("/items/lost/username")
    Call<ResponseBody> getItemsByUser(@Path("username") String username);

    //FormUrlEncoded
    @POST("/users")
    Call<ResponseBody> createUser(@Body RequestBody user);

    //FormUrlEncoded
    @POST("/items/found")
    Call<ResponseBody> createFoundItem(@Body RequestBody user);

    @DELETE("/user/{id}")
    Call<ResponseBody> deleteUser(@Path("id") String username);

    //Where the update for the user accessed date will be
    @PUT("/user/{username}?")
    Call<ResponseBody> updateAccessedDate(@Path("username") String username);
}