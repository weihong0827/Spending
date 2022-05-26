package com.example.spending;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class uploadReciept extends Fragment implements NoticeDialogFragment.NoticeDialogListener {
    //tag
    private static final String TAG = "uploadReciept";
    private UploadRecieptViewModel mViewModel;

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
        mViewModel = new ViewModelProvider(this).get(UploadRecieptViewModel.class);
        uploadReciept(view);

    }

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    Log.d(TAG, "onActivityResult: "+uri);
                    mViewModel.uploadReciept(uri, getActivity(), new UploadReceiptCallback() {
                        @Override
                        public void onSuccess() {
                            showNoticeDialog();
                        }

                        @Override
                        public void onFailure() {
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
    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new NoticeDialogFragment();

        dialog.show(((MainActivity) getContext()).getSupportFragmentManager(), "NoticeDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Toast.makeText(getContext(), "Uploading Reciept postitive", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(getContext(), "Uploading Reciept negative", Toast.LENGTH_SHORT).show();
    }
}