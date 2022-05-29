package com.example.spending.ui.expenditure;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public interface ExpensesCallback {
    void act(Task<QuerySnapshot> task);
}
