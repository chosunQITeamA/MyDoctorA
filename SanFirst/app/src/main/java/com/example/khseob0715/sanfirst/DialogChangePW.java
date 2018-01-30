package com.example.khseob0715.sanfirst;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Kim Jin Hyuk on 2018-01-30.
 */

public class DialogChangePW extends DialogFragment {
    private MyDialogListener myListener;
    public interface MyDialogListener {
        void myCallback(String cityName);
    }
    public DialogChangePW() {
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
        builder.setView(inflater.inflate(R.layout.dialog_changepw, null));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                EditText changepwOK = (EditText) getDialog().findViewById(R.id.dialog_change_pw);
                String changepwS = changepwOK.getText().toString();
                Toast.makeText(getActivity(), "PW : " + changepwS, Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }
}