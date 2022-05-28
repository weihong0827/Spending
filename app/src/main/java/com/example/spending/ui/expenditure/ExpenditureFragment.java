package com.example.spending.ui.expenditure;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.spending.R;
import com.example.spending.Shoppinglist.ShoppingListViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class ExpenditureFragment extends Fragment {

    private ExpenditureViewModel expViewModel;
    private ShoppingListViewModel mViewModel;
    private static final String TAG = "Expenditure";
    public static ExpenditureFragment newInstance() {
        return new ExpenditureFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        expViewModel = new ViewModelProvider(this).get(ExpenditureViewModel.class);

        View rootView = inflater.inflate(R.layout.expenditure_fragment, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText editTextBudget = (EditText) view.findViewById(R.id.budget_value);
        Button submit_budget_value = (Button) view.findViewById(R.id.submit_budget_value);
        TextView remaining_value = (TextView) view.findViewById(R.id.remaining_value);

        Log.d(TAG, "Before .getBudget");
        expViewModel.getBudget("1", new ExpenditureCallback() {
            @Override
            public void act(Task<QuerySnapshot> task) {
                Log.d(TAG, "Inside.getBudget");
                editTextBudget.setText(task.getResult().getDocuments().get(0).get("budget").toString());
            }
        });
        Log.d(TAG, "After .getBudget");

        Log.d(TAG, "expViewModel.display_remaining failing?");
        expViewModel.display_remaining("1", new ExpenditureCallback() {
                @Override
                public void act(Task<QuerySnapshot> task) {
                    float budget_value = Float.valueOf((editTextBudget.getText().toString()));
                    int expense = Integer.parseInt(task.getResult().getDocuments().get(0).get("expense").toString());
                    remaining_value.setText(String.valueOf(expViewModel.calculation(budget_value, expense)));
                }
            }
        );
        Log.d(TAG, "expViewModel.display_remaining success?");

        submit_budget_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float budget_value = Float.parseFloat(editTextBudget.getText().toString());
                expViewModel.addBudget("1", budget_value);
//                I don't need to use a callback for this right? I can just make use of the same var budget_value?
//                expViewModel.getBudget("1", new ExpenditureCallback() {
//                    @Override
//                    public void act(Task<QuerySnapshot> task) {
//                        editTextBudget.setText(task.getResult().getDocuments().get(0).get("budget").toString());
//                    }
//                });
                editTextBudget.setText(Float.toString(budget_value));
                expViewModel.display_remaining("1", new ExpenditureCallback() {
                            @Override
                            public void act(Task<QuerySnapshot> task) {
                                int number = 0;
                                int expense = Integer.parseInt(task.getResult().getDocuments().get(0).get("expense").toString());
                                remaining_value.setText(String.valueOf(expViewModel.calculation(budget_value, expense)));
                            }
                        }
                );
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        expViewModel = new ViewModelProvider(this).get(ExpenditureViewModel.class);
        // TODO: Use the ViewModel
    }
}