package com.example.spending.Network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {
    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request authenticatedRequest = request.newBuilder()
                            .header("Authorization", Credentials.basic("ahJutNYGGei5PU_wqsikbfNUu0msHVqt","")).build();
                    return chain.proceed(authenticatedRequest);
                }
            })
            .build();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://app.nanonets.com/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();
    RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

    public Call<NanonetsReceiptModal> sendReceipt(RequestBody body) {
        return retrofitAPI.sendReceipt(body);
    }
}
