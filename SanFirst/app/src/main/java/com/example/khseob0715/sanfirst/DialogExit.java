package com.example.khseob0715.sanfirst;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

/**
 * Created by Kim Jin Hyuk on 2018-01-30.
 */

public class DialogExit extends DialogFragment {
    private MyDialogListener myListener;
    public interface MyDialogListener {
        void myCallback(String cityName);
    }
    public DialogExit() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            myListener = (MyDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_exit, null));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                ActivityCompat.finishAffinity(getActivity());
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return builder.create();
    }
}