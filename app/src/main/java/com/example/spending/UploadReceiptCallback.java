package com.example.spending;

public interface UploadReceiptCallback {
    void onSuccess(String url);
    void onFailure();

}
