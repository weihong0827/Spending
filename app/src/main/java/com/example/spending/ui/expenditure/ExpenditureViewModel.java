package com.example.spending.ui.expenditure;

import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.example.spending.ui.expenditure.ExpenditureCallback;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;

public class ExpenditureViewModel extends ViewModel {

    private static final String TAG = "ExpenditureViewModel";

    //constructor
    FirebaseFirestore db;
    public ExpenditureViewModel() { db = FirebaseFirestore.getInstance(); }
//    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addBudget(String user_id, float budget_value) {
        HashMap<String, Object> budgetMap = new HashMap<>();
        budgetMap.put("budget_value", budget_value);
        budgetMap.put("user_id", user_id);
        // ##################################################################################### why can't firebase update? Is it cause my firebase not working?
        db.collection("budget").document("testUpdate").update(budgetMap);
        Log.d(TAG, "addBudget: tested doc().update");
        db.collection("budget").document("testSet").set(budgetMap);
        Log.d(TAG, "addBudget: tested doc().set");
        db.collection("budget").add(budgetMap);
        Log.d(TAG, "addBudget: tested col().add");
        // ##################################################################################### what determines successful?
        if (!db.collection("budget").whereEqualTo("user_id", user_id).get().isSuccessful()) {
            db.collection("budget").add(budgetMap);
            Log.d(TAG, "Added " + budget_value + " to firebase for user_id: " + user_id + " with Idk what document ID");
        } else {
            // test going into the else loop ###################################################################################################
            // how to updateDoc? ###################################################################################################
            db.collection("budget").document("budget_fixeddocumentid").set(budgetMap);
            db.collection("budget").whereEqualTo("user_id", user_id);
            Log.d(TAG, "addBudget: updated document to " + budget_value + " for user_id: " + user_id);
        }
    }

    public void getBudget(String user_id, final ExpenditureCallback callback) {
        //firebase firestore get record

        db.collection("budget").whereEqualTo("user_id", user_id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.act(task);
            }
        });
    }

    public void display_remaining(String user_id, final ExpenditureCallback callback) {
        db.collection("expense").whereEqualTo("user_id", user_id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.act(task);
            }
        });
    }

    public float calculation(float budget, float expenses) {
        return budget - expenses;
    }
}