package com.example.spending.ui.expenditure;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

public interface ExpenditureCallback {
    void act(Task<DocumentReference> task);
}
