package com.example.spending.ui.expenditure;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.example.spending.R;

public class Expenditure_java extends AppCompatActivity {

    EditText editTextBudget = (EditText) findViewById(R.id.budget_value);
    String text = editTextBudget.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenditure);

        editTextBudget = findViewById(R.id.budget_value);

    }
}
