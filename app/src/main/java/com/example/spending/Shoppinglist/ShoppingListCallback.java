package com.example.spending.Shoppinglist;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

interface ShoppingListGetItemCallback {

    void act(Task<QuerySnapshot> task);

}
interface ShoppingListCheckboxUpdateCallback {
    void act(Task<DocumentReference> task);
}