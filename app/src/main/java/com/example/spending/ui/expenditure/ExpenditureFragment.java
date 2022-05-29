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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spending.R;
import com.example.spending.Shoppinglist.ShoppingListViewModel;
import com.example.spending.data.model.ReceiptDetail;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExpenditureFragment extends Fragment {

    private ExpenditureViewModel expViewModel;
    private ShoppingListViewModel mViewModel;
    private static final String TAG = "Expenditure";
    float budget = 0;
    float total_spent = 0;
    FirebaseUser user;

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
        user = FirebaseAuth.getInstance().getCurrentUser();
        EditText editTextBudget = (EditText) view.findViewById(R.id.budget_value);
        Button submit_budget_value = (Button) view.findViewById(R.id.submit_budget_value);
        TextView remaining_value = (TextView) view.findViewById(R.id.remaining_value);
        if (user != null) {
            expViewModel.getBudget(user.getUid(), new ExpenditureCallback() {
                        @Override
                        public void act(Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                if (task.getResult().getDocuments().size() > 0) {
                                    if (!String.valueOf(task.getResult().getDocuments().get(0).get("budget_value")).isEmpty()) {
                                        budget = Float.parseFloat(String.valueOf(task.getResult().getDocuments().get(0).get("budget_value")));
                                        editTextBudget.setText(String.valueOf(budget));
                                        editTextBudget.setTag(task.getResult().getDocuments().get(0).getId());
                                    }

                                }
                                float finalBudget = budget;
                                expViewModel.getExpenses(user.getUid(), new ExpensesCallback() {
                                    @Override
                                    public void act(Task<QuerySnapshot> task) {
                                        float groceries_total_amount = 0;
                                        float furniture_prices = 0;
                                        float it_prices = 0;
                                        float daily_prices = 0;
                                        float others_prices = 0;
                                        if (task.isSuccessful()) {
                                            QuerySnapshot querySnapshot = task.getResult();
                                            if (querySnapshot.isEmpty()) {
                                                Log.d(TAG, "onViewCreated: No Receipts");
                                            } else {

                                                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                                    ReceiptDetail receipt = document.toObject(ReceiptDetail.class);
                                                    long epoch = receipt.getTimestamp();
                                                    //convert long time to local date time
                                                    String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(epoch));
                                                    ArrayList<ReceiptDetail.IndividualItem> items = receipt.getItems();
                                                    float total_amount = 0;
                                                    for (int j = 0; j < items.size(); j++) {
                                                        ReceiptDetail.IndividualItem item = items.get(j);
                                                        if (item.getLabel().equals("Total_Amount")) {
                                                            total_amount = Float.parseFloat(item.getOcr_text());
                                                            total_spent += total_amount;
                                                        }
                                                    }
                                                    switch (receipt.getCategory()) {
                                                        case "Groceries":
                                                            groceries_total_amount += total_amount;
                                                            break;
                                                        case "Furniture":
                                                            furniture_prices += total_amount;
                                                            break;
                                                        case "IT":
                                                            it_prices += total_amount;
                                                            break;
                                                        case "Daily":
                                                            daily_prices += total_amount;
                                                            break;
                                                        case "Others":
                                                            others_prices += total_amount;
                                                            break;
                                                    }
                                                }
                                                remaining_value.setText(String.valueOf(expViewModel.calculation(finalBudget, total_spent)));
                                            }
                                        }

                                        drawGraph(groceries_total_amount, furniture_prices, it_prices, daily_prices, others_prices, view);
                                    }

                                    ;
                                });
                            }


                        }
                    }
            );



        }else{
            editTextBudget.setText("0");
            remaining_value.setText("0");
            Toast.makeText(getActivity(), "Please login to view your expenses", Toast.LENGTH_SHORT).show();
        }


        submit_budget_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user!=null) {
                    float budget_value = Float.parseFloat(editTextBudget.getText().toString());
                    String tag = editTextBudget.getTag().toString();
                    if (!tag.isEmpty()) {
                        expViewModel.updateBudget(tag, budget_value);
                        remaining_value.setText(String.valueOf(expViewModel.calculation(budget_value, total_spent)));

                    }else{
                        expViewModel.addBudget(user.getUid(), budget_value);
                        editTextBudget.setText(String.valueOf(budget_value));
                        expViewModel.display_remaining(user.getUid(), new ExpenditureCallback() {
                                    @Override
                                    public void act(Task<QuerySnapshot> task) {
                                        float expense = Float.parseFloat(String.valueOf(task.getResult().getDocuments().get(0).get("expense")));
                                        remaining_value.setText(String.valueOf(expViewModel.calculation(budget_value, expense)));
                                    }
                                }
                        );
                    }

                }else{
                    Toast.makeText(getActivity(), "Please login to update your budget", Toast.LENGTH_SHORT).show();
                }

            }
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