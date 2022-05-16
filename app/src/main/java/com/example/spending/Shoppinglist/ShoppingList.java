package com.example.spending.Shoppinglist;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.spending.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ShoppingList extends Fragment {
    //tag
    private static final String TAG = "ShoppingList";
    private ShoppingListViewModel mViewModel;

    public static ShoppingList newInstance() {
        return new ShoppingList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);

        return inflater.inflate(R.layout.shopping_list_fragment, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button addItemButton = view.findViewById(R.id.addItem);
        EditText editText = view.findViewById(R.id.item);
        Spinner spinner = view.findViewById(R.id.category);
        ;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.Category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        LinearLayout linearLayout = view.findViewById(R.id.shopping_list_fragment_linear_layout);
        //insert checkbox in linear layout
        mViewModel.getItem("1", new ShoppingListGetItemCallback() {
            @Override
            public void act(Task<QuerySnapshot> task) {
                for (int i = 0; i < task.getResult().size(); i++) {
                    String item = task.getResult().getDocuments().get(i).get("item").toString();
                    String category = task.getResult().getDocuments().get(i).get("category").toString();
                    String document = task.getResult().getDocuments().get(i).getId();
                    Boolean checked = (Boolean) task.getResult().getDocuments().get(i).get("checked");
                    Log.d(TAG, "act: " + item + " " + category + " " + checked);
                    CheckBox checkBox = new CheckBox(getContext());
                    checkBox.setText(item);
                    checkBox.setTag(document);
                    checkBox.setChecked(checked);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                mViewModel.updateChecked(compoundButton.getTag().toString(), true);
                                Log.d(TAG, "onCheckedChanged: " + compoundButton.getText() + " is checked " + compoundButton.getTag());
                            }else{
                                mViewModel.updateChecked(compoundButton.getTag().toString(), false);
                                Log.d(TAG, "onCheckedChanged: " + compoundButton.getText() + " unchecked "+ compoundButton.getTag());
                            }
                        }
                    });
                    linearLayout.addView(checkBox);
                }
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String item = editText.getText().toString();
                String category = spinner.getSelectedItem().toString();
                    if (item.isEmpty()) {
                        Toast.makeText(getContext(), "Please enter an item", Toast.LENGTH_SHORT).show();
                    }else{

                        mViewModel.insertItem(item, category, "1", new ShoppingListCheckboxUpdateCallback() {
                            @Override
                            public void act(Task<DocumentReference> task) {
                                CheckBox checkBox = new CheckBox(getContext());
                                checkBox.setText(item);
                                checkBox.setTag(task.getResult().getId());

                                checkBox.setChecked(false);
                                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if (b) {
                                            mViewModel.updateChecked(compoundButton.getTag().toString(), true);
                                            Log.d(TAG, "onCheckedChanged: " + compoundButton.getText() + " is checked " + compoundButton.getTag());
                                        }else{
                                            mViewModel.updateChecked(compoundButton.getTag().toString(), false);
                                            Log.d(TAG, "onCheckedChanged: " + compoundButton.getText() + " unchecked "+ compoundButton.getTag());
                                        }
                                    }
                                });
                                editText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                                editText.getText().clear();
                                linearLayout.addView(checkBox);
                            }
                        });
                    }

            }
        });

    }
}