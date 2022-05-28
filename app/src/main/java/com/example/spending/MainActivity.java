package com.example.spending;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.spending.databinding.ActivityMainBinding;

import android.graphics.Color;
import android.widget.TextView;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class MainActivity extends AppCompatActivity {

    TextView tvGroceries, tvFurniture, tvIT, tvDailyNecessities, tvOthers;
    PieChart pieChart;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_shoppingList, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        setContentView(R.layout.expenditure_fragment);

        // Link those objects with their
        // respective id's that
        // we have given in .XML file
        tvGroceries = findViewById(R.id.tvGroceries);
        tvFurniture = findViewById(R.id.tvFurniture);
        tvIT = findViewById(R.id.tvIT);
        tvDailyNecessities = findViewById(R.id.tvDailyNecessities);
        tvOthers = findViewById(R.id.tvOthers);
        pieChart = findViewById(R.id.piechart);

        // Creating a method setData()
        // to set the text in text view and pie chart
        setData();

    }

    @SuppressLint("SetTextI18n")
    private void setData()
    {

        // Set the percentage of language used
        tvGroceries.setText(Integer.toString(40));
        tvFurniture.setText(Integer.toString(30));
        tvIT.setText(Integer.toString(5));
        tvDailyNecessities.setText(Integer.toString(15));
        tvOthers.setText(Integer.toString(10));


        // Set the data and color to the pie chart
        pieChart.addPieSlice(
                new PieModel(
                        "Groceries",
                        Integer.parseInt(tvGroceries.getText().toString()),
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        "Furniture",
                        Integer.parseInt(tvFurniture.getText().toString()),
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        "IT",
                        Integer.parseInt(tvIT.getText().toString()),
                        Color.parseColor("#EF5350")));
        pieChart.addPieSlice(
                new PieModel(
                        "DailyNecessities",
                        Integer.parseInt(tvDailyNecessities.getText().toString()),
                        Color.parseColor("#29B6F6")));
        pieChart.addPieSlice(
                new PieModel(
                        "Others",
                        Integer.parseInt(tvOthers.getText().toString()),
                        Color.parseColor("#f629a4")));

        // To animate the pie chart
        pieChart.startAnimation();
    }

}


