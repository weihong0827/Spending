package com.example.spending;

import com.google.android.gms.tasks.Task;

public interface ShoppingListCallback {
    void act(Task<Void> task);
}
