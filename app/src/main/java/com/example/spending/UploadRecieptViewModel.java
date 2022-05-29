package com.example.spending;

import android.net.Uri;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

import com.example.spending.Network.NanonetsReceiptModal;
import com.example.spending.Network.ResponseReceiptModal;
import com.example.spending.Network.RetrofitRequest;
import com.example.spending.data.model.ReceiptDetail;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

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
    public void OcrRequest(String url, Callback<NanonetsReceiptModal> callback) {
        RetrofitRequest retrofitRequest = new RetrofitRequest();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("urls", url)
                .build();
        Call<NanonetsReceiptModal> call = retrofitRequest.sendReceipt(body);
        call.enqueue(callback);
    }
    public void uploadDetails(ReceiptDetail detail, UploadDetailCallback callback) {

        db.collection("receipts").add(detail).addOnSuccessListener(documentReference -> {
            callback.onSuccess();
        }).addOnFailureListener(e -> {
            callback.onFailure();
        });
    }
    public void getReceipts(String user_id,ReceiptCallback callback) {
        db.collection("receipts").whereEqualTo("user_id", user_id).orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + " => " + document.getData());

                }
                callback.act(task);
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

                callback.onSuccess(url);

            });

        });


        }


}