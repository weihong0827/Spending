package com.example.spending.Network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {
    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .build();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.ocr.space")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();
    RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

    public Call<ResponseReceiptModal> sendReceipt(RequestBody body) {
        return retrofitAPI.sendReceipt(body);
    }
}
