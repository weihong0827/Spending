package com.example.spending.ui.expenditure;

import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ExpenditureViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private static final String TAG = "ExpenditureViewModel";

    //constructor
    FirebaseFirestore db;
    public ExpenditureViewModel() { db = FirebaseFirestore.getInstance(); }
//    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addBudget(String budget_value) {
        HashMap<String, Object> budgetMap = new HashMap<>();
        budgetMap.put("budget", budget_value);
        db.collection("budget").document("budget_fixeddocumentid").set(budgetMap);
        System.out.println("system Added " + budget_value + " to firebase");
//                .set(task -> {
//            if (task.isSuccessful()) {
//                Log.d(TAG, "logcat Added " + budget_value + " to firebase with ID: " + task.getResult().getId());
//                System.out.println("system Added " + budget_value + " to firebase with ID: " + task.getResult().getId());
//            })
    }

//    public int getBudget() {
//        int value = db.collection("budget").document("budget_fixeddocumentid").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        System.out.println("DocumentSnapshot data: " + document.getData());
//                    } else {
//                        System.out.println("No such document");
//                    }
//                } else {
//                    System.out.println("get failed with " + task.getException());
//                }
//            }
//           });
//
//        return value;
//    }

    public String getBudget2() {
        System.out.println("Entered");
        Task<DocumentSnapshot> object = db.collection("budget").document("budget_fixeddocumentid").get();
        System.out.println(object);
        Object value = object.getResult().getData();
        System.out.println(value);
        return String.valueOf(value);
    }
        ;

//    public void updateBudget(String budget_value) {
//        db.collection("budget").document("budget_fixeddocumentid").update("budget", budget_value);
//    }

    public void display_remaining(TextView textView) {
        int budget = 200; // supposed to replace with expViewModel.get();
        int expenses = 50; // supposed to replace with expViewModel.get();
        int remaining_value = calculation(budget, expenses);
        textView.setText(String.valueOf(remaining_value));
    }

    public int calculation(int budget, int expenses) {
        return budget - expenses;
    }
}
