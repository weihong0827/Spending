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

    TextView tvGroceries, tvFurniture, tvIT, tvDailyNecessities, tvOthers;
    PieChart pieChart;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expViewModel = new ViewModelProvider(this).get(ExpenditureViewModel.class);

        EditText editTextBudget = (EditText) view.findViewById(R.id.budget_value);
        Button submit_budget_value = (Button) view.findViewById(R.id.submit_budget_value);
        TextView remaining_value = (TextView) view.findViewById(R.id.remaining_value);



        expViewModel.getBudget("1", new ExpenditureCallback() {
                    @Override
                    public void act(Task<QuerySnapshot> task) {
                        if (!String.valueOf(task.getResult().getDocuments().get(0).get("budget_value")).isEmpty()) {
                            editTextBudget.setText(String.valueOf(task.getResult().getDocuments().get(0).get("budget_value")));
                        }

                        expViewModel.display_remaining("1", new ExpenditureCallback() {
                                    @Override
                                    public void act(Task<QuerySnapshot> task) {
                                        if (!String.valueOf(editTextBudget.getText()).equals("null")) {
                                            float budget_value = Float.parseFloat(String.valueOf(editTextBudget.getText()));
//                                            float expense = Float.parseFloat(String.valueOf(task.getResult().getDocuments().get(0).get("expense")));
                                            float expense = 50.0f;
                                            remaining_value.setText(String.valueOf(expViewModel.calculation(budget_value, expense)));
                                        } else {
                                            // can use try {
                                            //      } catch {
                                            //      while (budget_value == Null) {
                                            //          logd("Still Null");
                                            //          delay(3000);
                                            //          Float.parseFloat(...);
                                            //          }
                                            //      } finally {                // don't know if need use finally or not
                                            //      float expense =
                                            //      remaining_value.setText(...)
                                            //      }
                                            Log.d(TAG, "act: editTextBudget is Null");
                                        }
                                        Log.d(TAG, "Expenditure page finished loading");
                                    }
                                }
                        );
                    }
                }
        );

        submit_budget_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float budget_value = Float.parseFloat(editTextBudget.getText().toString());
                expViewModel.addBudget("1", budget_value);
                editTextBudget.setText(String.valueOf(budget_value));
                expViewModel.display_remaining("1", new ExpenditureCallback() {
                            @Override
                            public void act(Task<QuerySnapshot> task) {
                                float expense = Float.parseFloat(String.valueOf(task.getResult().getDocuments().get(0).get("expense")));
                                remaining_value.setText(String.valueOf(expViewModel.calculation(budget_value, expense)));
                            }
                        }
                );
            }
        });

        expViewModel.getExpenses("1", new ExpensesCallback() {
            @Override
            public void act(Task<QuerySnapshot> task) {
                float groceries_total_amount = 0;
                float furniture_prices = 0;
                float it_prices = 0;
                float daily_prices = 0;
                float others_prices = 0;
                for (int i = 0; i < task.getResult().size(); i++) {
                    // split each document into different categories

                    String category = task.getResult().getDocuments().get(i).get("category").toString();
                    if (category.equals("Groceries")) {
                        groceries_total_amount += Float.parseFloat(String.valueOf(task.getResult().getDocuments().get(i).get("price")));
                    }else if (category.equals("Furniture")) {
                        furniture_prices += Float.parseFloat(String.valueOf(task.getResult().getDocuments().get(i).get("price")));
                    }else if (category.equals("IT")) {
                        it_prices += Float.parseFloat(String.valueOf(task.getResult().getDocuments().get(i).get("price")));
                    } else if (category.equals("Daily")) {
                        daily_prices += Float.parseFloat(String.valueOf(task.getResult().getDocuments().get(i).get("price")));
                    }else{
                        others_prices += Float.parseFloat(String.valueOf(task.getResult().getDocuments().get(i).get("price")));
                    }
                    // repeat either one of the above for other categories once its confirm working #####################################################
                } ;
                drawGraph(groceries_total_amount,furniture_prices,it_prices,daily_prices,others_prices,view);
            }

            ;
        });


    };
    // why need to initialise input variables to be 0? ################################################################
    public void drawGraph(float groceries_prices, float furniture_prices, float it_prices, float daily_prices, float others_prices,View view) {

        // Link those objects with their
        // respective id's that
        // we have given in .XML file
        tvGroceries = view.findViewById(R.id.tvGroceries);
        tvFurniture = view.findViewById(R.id.tvFurniture);
        tvIT = view.findViewById(R.id.tvIT);
        tvDailyNecessities = view.findViewById(R.id.tvDailyNecessities);
        tvOthers = view.findViewById(R.id.tvOthers);
        pieChart = view.findViewById(R.id.piechart);

        // to set the text in text view and pie chart
        // Set the percentage of language used
        tvGroceries.setText(Float.toString(groceries_prices));
        tvFurniture.setText(Float.toString(furniture_prices));
        tvIT.setText(Float.toString(it_prices));
        tvDailyNecessities.setText(Float.toString(daily_prices));
        tvOthers.setText(Float.toString(others_prices));

        // Set the data and color to the pie chart
        pieChart.addPieSlice(
                new PieModel(
                        "Groceries",
                        groceries_prices,
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        "Furniture",
                        furniture_prices,
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        "IT",
                        it_prices,
                        Color.parseColor("#EF5350")));
        pieChart.addPieSlice(
                new PieModel(
                        "DailyNecessities",
                        daily_prices,
                        Color.parseColor("#29B6F6")));
        pieChart.addPieSlice(
                new PieModel(
                        "Others",
                        others_prices,
                        Color.parseColor("#f629a4")));

        // To animate the pie chart
        pieChart.startAnimation();
    }
}