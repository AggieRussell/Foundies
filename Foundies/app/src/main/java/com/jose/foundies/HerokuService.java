package com.jose.foundies;

/**
 * Created by kylepreston on 3/27/17.
 */

import java.util.Map;
import java.util.Observable;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface HerokuService {
    @GET("/hello")
    Call<ResponseBody> hello();

    @GET("/users")
    Call<ResponseBody> getUsers();

    @GET("/questions")
    Call<ResponseBody> getQuestions();

    @GET("/questions/qs?")
    Call<ResponseBody> getQuestionsWithQs(@Query("q1") String q1, @Query("q2") String q2);

    @GET("/items/found/bycategories/")
    Call<ResponseBody> getFoundItemWithCategories(@Query("cc") String q1, @Query("sc") String q2);

    @GET("/items/lost/bycategories/")
    Call<ResponseBody> getLostItemWithCategories(@Query("cc") String q1, @Query("sc") String q2);

    @GET("/user/{username}")
    Call<ResponseBody> getUsersByUsername(@Path("username") String username);

    @GET("/items/found")
    Call<ResponseBody> getFoundItems();

    @GET("/chatMessage")
    Call<ResponseBody> getMessage();

    //Make sure to implement
    @GET("/chatMessage/{receiver}")
    Call<ResponseBody> getMessagesbyUser(@Path("receiver") String receiver);

    //Todo: Finish implementing getFoundItemsByUser
    @GET("/items/found/{username}")
    Call<ResponseBody> getFoundItemsByUser(@Path("username") String username);

    @GET("/items/lost/{username}")
    Call<ResponseBody> getLostItemsByUser(@Path("username") String username);

    //FormUrlEncoded
    @POST("/users")
    Call<ResponseBody> createUser(@Body RequestBody user);

    //FormUrlEncoded
    @POST("/items/found")
    Call<ResponseBody> createFoundItem(@Body RequestBody user);

    //FormUrlEncoded
    @POST("/items/lost")
    Call<ResponseBody> createLostItem(@Body RequestBody user);

    @POST("/chatMessage")
    Call<ResponseBody> createMessage(@Body RequestBody chatMessage);

    @DELETE("/user/{id}")
    Call<ResponseBody> deleteUser(@Path("id") String username);

    @DELETE("/remove/lost/{id}")
    Call<ResponseBody> deleteLostItem(@Path("id") String username);

    @DELETE("/remove/found/{id}")
    Call<ResponseBody> deleteFoundItem(@Path("id") String username);

    @DELETE("/remove/user/{id}")
    Call<ResponseBody> deleteUserById(@Path("id") String id);

    @DELETE("/remove/chatMessage/{id}")
    Call<ResponseBody> deleteMessage(@Path("id") String id);

    //Where the update for the user accessed date will be
    @PUT("/user/{username}?")
    Call<ResponseBody> updateUser(@Path("username") String username, @QueryMap Map<String, String> params);

    @PUT("/user/queryLost/{username}?")
    Call<ResponseBody> updateUserQueryLost(@Path("username") String username, @QueryMap Map<String, String> params);

    @PUT("/user/queryFound/{username}?")
    Call<ResponseBody> updateUserQueryFound(@Path("username") String username, @QueryMap Map<String, String> params);

    //Updates the query_lost_count, query_found_count, and the last_accessed fields in the database
    //Must sends ALL 3 in the Map<String, String> for it to work properly
    // PUT operation for updating the date the app was last accessed by a user
    @PUT("/user/lastAccessed/{username}?")
    Call<ResponseBody> updateUserLastAccessed(@Path("username") String username, @QueryMap Map<String, String> params);

    @PUT("/user/update/{username}?")
    Call<ResponseBody> updateUserInfo(@Path("username") String username, @QueryMap Map<String, String> params);


}