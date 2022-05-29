package com.example.spending.Network;

import okhttp3.RequestBody;
import retrofit2.Call;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitAPI {
    //apikey
    String API_KEY = "K82307679888957";
    @POST("parse/image")
    @Headers("apikey: " + API_KEY)
    Call<ResponseReceiptModal> sendReceipt(@Body RequestBody body);
}
