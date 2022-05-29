package com.example.spending;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

import com.example.spending.Network.ReceiptModal;
import com.example.spending.Network.ResponseReceiptModal;
import com.example.spending.Network.RetrofitRequest;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class UploadRecieptViewModel extends ViewModel {
    //tag
    private static final String TAG = "UploadRecieptViewModel";

    // get firebase reference
    FirebaseFirestore db;
    private StorageReference mStorageRef;
    public UploadRecieptViewModel() {
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }
    public void OcrRequest(Uri uri) {
        RetrofitRequest retrofitRequest = new RetrofitRequest();
        ReceiptModal receiptModal = new ReceiptModal("https://firebasestorage.googleapis.com/v0/b/spending-21525.appspot.com/o/images%2F31640952-7630-4ed3-bd84-3830f73b7c24?alt=media&token=cf66a557-7a43-4caa-87f6-3085d92d1ff2");
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("url","https://firebasestorage.googleapis.com/v0/b/spending-21525.appspot.com/o/images%2F31640952-7630-4ed3-bd84-3830f73b7c24?alt=media&token=cf66a557-7a43-4caa-87f6-3085d92d1ff2")
                .build();
        Call<ResponseReceiptModal> call = retrofitRequest.sendReceipt(body);
        call.enqueue(new retrofit2.Callback<ResponseReceiptModal>() {
            @Override
            public void onResponse(Call<ResponseReceiptModal> call, retrofit2.Response<ResponseReceiptModal> response) {
                Log.d(TAG, "onResponse: "+response.body().toString());
                Log.d(TAG, "onResponse: ConfigurationListener::"+call.request().body());
            }

            @Override
            public void onFailure(Call<ResponseReceiptModal> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    public void uploadReciept(Uri uri, FragmentActivity activity,UploadReceiptCallback callback) {

        String name = UUID.randomUUID().toString();
        StorageReference ref
                = mStorageRef
                .child("images/" + name);
        ref.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            // Get a URL to the uploaded content
            Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
            downloadUrl.addOnSuccessListener(uri1 -> {
                String url = uri1.toString();
                Log.d(TAG, "uploadReciept: "+url);
                callback.onSuccess();

            });

        });


        }


}