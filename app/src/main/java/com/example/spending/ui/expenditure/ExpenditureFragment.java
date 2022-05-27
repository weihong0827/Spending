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
import android.widget.EditText;
import android.widget.TextView;

import com.example.spending.R;
import com.example.spending.Shoppinglist.ShoppingListViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
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
        String existing_budget_value = expViewModel.getBudget("1", new ExpenditureCallback() {
            @Override
            public void act(Task<DocumentReference> task) {
                System.out.println(task);
            }
        }); // supposed to replace with expViewModel.getBudget2();
        editTextBudget.setText(existing_budget_value);
        editTextBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!editTextBudget.getText().toString().equals("")) {
//                    System.out.println("entered");
//                    String budget_value = editTextBudget.getText().toString();
//                    TextView remaining = (TextView) view.findViewById(R.id.remaining_value);
//                    int v = Integer.parseInt(budget_value);
//                    int total_amount = 50;
//                    int remaining_value = v - total_amount;
//                    remaining.setText(String.valueOf(remaining_value));
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String budget_value = editTextBudget.getText().toString();
                expViewModel.addBudget(budget_value);
                Log.d(TAG, "addedBudget afterTextChanged");
                TextView textView = (TextView) view.findViewById(R.id.remaining_value);
                textView. setText(50-20);
                expViewModel.display_remaining(textView);
            }
        });
//        v = budget_value - hisViewModel.getItem(total_amount)
//        TextView.setText(v);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        expViewModel = new ViewModelProvider(this).get(ExpenditureViewModel.class);
        // TODO: Use the ViewModel
    }
}