package com.example.spending.ui.expenditure;


import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.graphics.Color;
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

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
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

//        expViewModel.getBudget("1", new ExpenditureCallback() {
//                @Override
//                public void act(Task<QuerySnapshot> task) {
//                    Log.d(TAG, "2nd callback");
//                    if (!String.valueOf(task.getResult().getDocuments().get(0).get("budget")).isEmpty()) {
//                        editTextBudget.setText(String.valueOf(task.getResult().getDocuments().get(0).get("budget")));
//                    }
//                    Log.d(TAG, "end of 2nd callback");
//
//                    expViewModel.display_remaining("1", new ExpenditureCallback() {
//                                @Override
//                                public void act(Task<QuerySnapshot> task) {
//                                    Log.d(TAG, "first callback");
//                                    if (!editTextBudget.getText().toString().isEmpty()) {
//                                        float budget_value = Float.parseFloat(String.valueOf(editTextBudget.getText()));
//                                        float expense = Float.parseFloat(String.valueOf(task.getResult().getDocuments().get(0).get("expense")));
//                                        remaining_value.setText(String.valueOf(expViewModel.calculation(budget_value, expense)));
//                                    }
//                                    Log.d(TAG, "end of first callback");
//                                }
//                            }
//                    );
//
//                }
//            }
//        );
//
//        submit_budget_value.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                float budget_value = Float.parseFloat(editTextBudget.getText().toString());
//                expViewModel.addBudget("1", budget_value);
////                I don't need to use a callback for this right? I can just make use of the same var budget_value?
////                expViewModel.getBudget("1", new ExpenditureCallback() {
////                    @Override
////                    public void act(Task<QuerySnapshot> task) {
////                        editTextBudget.setText(task.getResult().getDocuments().get(0).get("budget").toString());
////                    }
////                });
//                editTextBudget.setText(String.valueOf(budget_value));
//                expViewModel.display_remaining("1", new ExpenditureCallback() {
//                            @Override
//                            public void act(Task<QuerySnapshot> task) {
//                                float expense = Float.parseFloat(String.valueOf(task.getResult().getDocuments().get(0).get("expense")));
//                                remaining_value.setText(String.valueOf(expViewModel.calculation(budget_value, expense)));
//                            }
//                        }
//                );
//            }
//        });
//
        editTextBudget.setText("Why isn't this working?");
        Log.d(TAG, "onViewCreated: Finished .setText for editText");
//
//        TextView CtvGroceries = (TextView) view.findViewById(R.id.tvGroceries);
//        CtvGroceries.setText("400");
//        Log.d(TAG, "onViewCreated: Finished .setText for tvGroceries");
//
//        TextView tvGroceries, tvFurniture, tvIT, tvDailyNecessities, tvOthers;
//        PieChart pieChart;
//
//        // Link those objects with their
//        // respective id's that
//        // we have given in .XML file
//        tvGroceries = (TextView) view.findViewById(R.id.tvGroceries);
//        tvFurniture = view.findViewById(R.id.tvFurniture);
//        tvIT = view.findViewById(R.id.tvIT);
//        tvDailyNecessities = view.findViewById(R.id.tvDailyNecessities);
//        tvOthers = view.findViewById(R.id.tvOthers);
//        pieChart = view.findViewById(R.id.piechart);
//        Log.d(TAG, "onViewCreated: Initialised TextViews for pie chart");
//
//        // Set the percentage of language used
//        tvGroceries.setText(Integer.toString(40));
//        tvFurniture.setText(Integer.toString(30));
//        tvIT.setText(Integer.toString(5));
//        tvDailyNecessities.setText(Integer.toString(15));
//        tvOthers.setText(Integer.toString(10));
//        Log.d(TAG, "onViewCreated: Finished .setText of views");
//
//        // Creating a method setData()
//        // to set the text in text view and pie chart
//        // Set the data and color to the pie chart
//        pieChart.addPieSlice(
//                new PieModel(
//                        "Groceries",
//                        Integer.parseInt(tvGroceries.getText().toString()),
//                        Color.parseColor("#FFA726")));
//        Log.d(TAG, "onViewCreated: Added a Pie Slice");
//        pieChart.addPieSlice(
//                new PieModel(
//                        "Furniture",
//                        Integer.parseInt(tvFurniture.getText().toString()),
//                        Color.parseColor("#66BB6A")));
//        pieChart.addPieSlice(
//                new PieModel(
//                        "IT",
//                        Integer.parseInt(tvIT.getText().toString()),
//                        Color.parseColor("#EF5350")));
//        pieChart.addPieSlice(
//                new PieModel(
//                        "DailyNecessities",
//                        Integer.parseInt(tvDailyNecessities.getText().toString()),
//                        Color.parseColor("#29B6F6")));
//        pieChart.addPieSlice(
//                new PieModel(
//                        "Others",
//                        Integer.parseInt(tvOthers.getText().toString()),
//                        Color.parseColor("#f629a4")));
//        Log.d(TAG, "onViewCreated: Added all Pie Slices");
//        // To animate the pie chart
//        pieChart.startAnimation();
//        Log.d(TAG, "onViewCreated: Started animation");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        expViewModel = new ViewModelProvider(this).get(ExpenditureViewModel.class);
        // TODO: Use the ViewModel
    }
}