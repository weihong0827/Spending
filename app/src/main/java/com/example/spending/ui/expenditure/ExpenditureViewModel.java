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
    public ExpenditureViewModel() {
        db = FirebaseFirestore.getInstance();
    }


}