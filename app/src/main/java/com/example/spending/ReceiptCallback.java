package com.example.spending;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;



public interface ReceiptCallback {
    void act(Task<QuerySnapshot> task);

}
