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

    @POST("OCR/Model/a9e0b2b3-af98-4663-b907-8853e38ea383/LabelUrls/")

    Call<NanonetsReceiptModal> sendReceipt(@Body RequestBody body);
}
