package com.example.spending;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.spending.Network.NanonetsReceiptModal;
import com.example.spending.data.model.ReceiptDetail;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class uploadReciept extends Fragment  {
    //tag
    private static final String TAG = "uploadReciept";
    private UploadRecieptViewModel mViewModel;
    FirebaseUser user;
    private boolean show;
    public static uploadReciept newInstance() {
        return new uploadReciept();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.upload_reciept_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mViewModel = new ViewModelProvider(this).get(UploadRecieptViewModel.class);
        Button upload = view.findViewById(R.id.addItemButton);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user!=null) {
                    uploadReciept(v);
                }else{
                    Toast.makeText(getContext(),"Please Login to upload",Toast.LENGTH_SHORT).show();

                }
            }
        });
        if  (user != null) {
            Log.d(TAG, "onViewCreated:"+user.getUid());
            mViewModel.getReceipts(user.getUid(), new ReceiptCallback() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void act(Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot.isEmpty()) {
                            Log.d(TAG, "onViewCreated: No Receipts");
                        } else {
                            Log.d(TAG, "onViewCreated: Receipts found");
                            LinearLayout linearLayout = view.findViewById(R.id.itemLinearLayout);
                            for (int i = 0; i < querySnapshot.getDocuments().size(); i++) {
                                ReceiptDetail receipt = querySnapshot.getDocuments().get(i).toObject(ReceiptDetail.class);
                                long epoch = receipt.getTimestamp();
                                //convert long time to local date time
                                String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date (epoch));
                                TextView textView = new TextView(getContext());
                                textView.setText(date);
                                Button button = new Button(getContext());
                                button.setText("View");
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Log.d(TAG, "onClick: Viewing Receipt");
                                        if (!show){
                                            show = true;
                                            ImageView imageView = new ImageView(getContext());
                                            imageView.setId(R.id.image_view);
                                            Glide.with(v).load(receipt.getImageUrl()).into(imageView);
                                            linearLayout.addView(imageView);
                                        }else{
                                            show = false;
                                            ImageView imageView = view.findViewById(R.id.image_view);
                                            linearLayout.removeView(imageView);
                                        }
                                    }
                                });
                                linearLayout.addView(textView);
                                ArrayList<ReceiptDetail.IndividualItem> items = receipt.getItems();
                                for (int j = 0; j < items.size(); j++) {
                                    ReceiptDetail.IndividualItem item = items.get(j);
                                    if (item.getLabel().equals("Total_Amount")) {
                                        TextView total = new TextView(getContext());
                                        total.setText("Total Amount: " + item.getOcr_text());
                                        total.setTextSize(20);
                                        linearLayout.addView(total);
                                    }
                                }
                                linearLayout.addView(button);


                            }

                        }
                    } else {
                        Log.d(TAG, "onViewCreated: Error getting receipts");
                    }
                }
            });
        }
//        if (user == null) {
//
//            Toast.makeText(getContext(), "Please login to continue", Toast.LENGTH_SHORT).show();
//            NavController navController = Navigation.findNavController(view);
//            navController.navigate(R.id.loginFragment);
//        }


    }

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    Log.d(TAG, "onActivityResult: "+uri);
                    final ProgressBar loadingProgressBar = getActivity().findViewById(R.id.progressBar);
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    Spinner spinner = getActivity().findViewById(R.id.uploadCategorySpinner);

                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.Category, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    mViewModel.uploadReciept(uri, getActivity(), new UploadReceiptCallback() {
                        @Override
                        public void onSuccess(String url) {

                            Log.d(TAG, "onSuccess: Success");
                            mViewModel.OcrRequest(url,new retrofit2.Callback<NanonetsReceiptModal>() {
                                @Override
                                public void onResponse(Call<NanonetsReceiptModal> call, Response<NanonetsReceiptModal> response) {
                                    loadingProgressBar.setVisibility(View.GONE);
                                    Log.d(TAG, "onResponse: "+response.body().toString());
                                    loadingProgressBar.setVisibility(View.GONE);
                                    getActivity().findViewById(R.id.scrollable1).setVisibility(View.GONE);
                                    getActivity().findViewById(R.id.linearLayout1).setVisibility(View.VISIBLE);
                                    getActivity().findViewById(R.id.addItemButton).setVisibility(View.GONE);
                                    if (response.body().getMessage().equals("Success")){


                                        ArrayList<NanonetsReceiptModal.ReceiptResult> result = response.body().getResult();

                                        if (result.size()>0){

                                            ArrayList<NanonetsReceiptModal.ReceiptResult.Prediction> predictions = result.get(0).getPrediction();

                                            LinearLayout linearLayout = getActivity().findViewById(R.id.linearLayout);

                                            for (NanonetsReceiptModal.ReceiptResult.Prediction prediction: predictions){

                                                LinearLayout ll = new LinearLayout(getContext());
                                                ll.setOrientation(LinearLayout.VERTICAL);
                                                TextView textView = new TextView(getContext());
                                                textView.setText(prediction.getLabel());
                                                ll.addView(textView);
                                                EditText editText = new EditText(getContext());
                                                editText.setText(prediction.getOcr_text());
                                                ll.addView(editText);
                                                Button Delete = new Button(getContext());
                                                Delete.setText("Delete");
                                                Delete.setOnClickListener(
                                                        new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                ll.setVisibility(View.GONE);
                                                            }
                                                        }
                                                );
                                                ll.setPadding(10,10,10,10);
                                                ll.addView(Delete);
                                                linearLayout.addView(ll);
                                            }
                                            Button button = getActivity().findViewById(R.id.uploadButton);
                                            button.setOnClickListener(
                                                    new View.OnClickListener() {
                                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                                        @Override
                                                        public void onClick(View v) {
                                                            ArrayList<ReceiptDetail.IndividualItem> items = new ArrayList<>();
                                                            for (int i=0;i<linearLayout.getChildCount();i++){
                                                                LinearLayout ll = (LinearLayout) linearLayout.getChildAt(i);
                                                                EditText editText = (EditText) ll.getChildAt(1);
                                                                TextView textView = (TextView) ll.getChildAt(0);
                                                                ReceiptDetail.IndividualItem item = new ReceiptDetail.IndividualItem(textView.getText().toString(), editText.getText().toString());
                                                                items.add(item);
                                                            }
                                                            ReceiptDetail receiptDetail = new ReceiptDetail(user.getUid(),items, System.currentTimeMillis(),url,spinner.getSelectedItem().toString());
                                                            mViewModel.uploadDetails(receiptDetail, new UploadDetailCallback() {
                                                                @Override
                                                                public void onSuccess() {
                                                                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                                                    //refresh fragment
                                                                }

                                                                @Override
                                                                public void onFailure() {
                                                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }
                                                    });

                                            Button cancelButton = getActivity().findViewById(R.id.cancel_button);
                                            cancelButton.setOnClickListener(
                                                    new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            getActivity().findViewById(R.id.scrollable1).setVisibility(View.VISIBLE);
                                                            getActivity().findViewById(R.id.linearLayout1).setVisibility(View.GONE);
                                                            getActivity().findViewById(R.id.addItemButton).setVisibility(View.VISIBLE);
                                                        }
                                                    });
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<NanonetsReceiptModal> call, Throwable t) {
                                    Log.d(TAG, "onFailure: "+t.getMessage());
                                    loadingProgressBar.setVisibility(View.GONE);
                                }
                            });
                        }

                        @Override
                        public void onFailure() {
                            loadingProgressBar.setVisibility(View.GONE);
                            Log.d(TAG, "onFailure: Failed");
                        }
                    });
                }


            });

    public void uploadReciept(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mGetContent.launch(intent);
    }

}