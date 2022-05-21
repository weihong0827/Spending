package com.example.spending.ui.expenditure;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.HashMap;

public class ExpenditureViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private static final String TAG = "ExpenditureViewModel";

    //constructor
    FirebaseFirestore db;
    public ExpenditureViewModel() { db = FirebaseFirestore.getInstance(); }
//    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addBudget(String budget_value) {
        HashMap<String, Object> itemMap = new HashMap<>();
        itemMap.put("budget", budget_value);
        db.collection("budget").add(itemMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "logcat Added " + budget_value + " to firebase with ID: " + task.getResult().getId());
                System.out.println("system Added " + budget_value + " to firebase with ID: " + task.getResult().getId());
            }

});}}