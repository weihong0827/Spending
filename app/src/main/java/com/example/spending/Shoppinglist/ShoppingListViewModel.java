package com.example.spending.Shoppinglist;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.util.HashMap;


public class ShoppingListViewModel extends ViewModel {
    //TAG
    private static final String TAG = "ShoppingListViewModel";

    //constructor
    FirebaseFirestore db;
    public ShoppingListViewModel() {
        db = FirebaseFirestore.getInstance();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertItem(String item, String category,String user_id, final ShoppingListCheckboxUpdateCallback callback) {
        //firebase firestore insert record

        LocalDateTime now = LocalDateTime.now();
        HashMap<String, Object> itemMap = new HashMap<>();
        itemMap.put("item", item);
        itemMap.put("category", category);
        itemMap.put("user_id", user_id);
        itemMap.put("Time", now);
        itemMap.put("checked", false);
        db.collection("shopping list").add(itemMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + task.getResult().getId());
                callback.act(task);
            }
        });
    }

    

    //get item from firebase firestore
    public void getItem(String user_id, final ShoppingListGetItemCallback callback) {
        //firebase firestore get record

        db.collection("shopping list").whereEqualTo("user_id", user_id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + " => " + document.getData());

                }
                callback.act(task);
            }
        });
    }
//    public void updateItem(String document, final ShoppingListCallback callback) {
//
//    }

    public void updateChecked(String document, boolean b) {
        db.collection("shopping list").document(document).update("checked", b);
    }
}