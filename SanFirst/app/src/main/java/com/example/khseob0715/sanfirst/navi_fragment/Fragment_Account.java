package com.example.khseob0715.sanfirst.navi_fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.ServerConn.ChangePW;
import com.example.khseob0715.sanfirst.ServerConn.DeleteAccount;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Account extends Fragment {

    private EditText newPW, confirm_newPW;
    private String pw;

    ChangePW changepw = new ChangePW();
    DeleteAccount delaccount = new DeleteAccount();

    public Fragment_Account() {
        // Required empty public constructor
    }

    int usn = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_account, container, false);

        Button deleteBtn = (Button)rootView.findViewById(R.id.idDeleteBtn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account_delete();
            }
        });

        Button updateBtn = (Button)rootView.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account_updatePW();
            }
        });

        usn = getArguments().getInt("usn");
        pw = getArguments().getString("pw");
        Log.e("Account usn = " , String.valueOf(usn) +"/"+ pw);

        newPW = (EditText) rootView.findViewById(R.id.newPW);
        confirm_newPW = (EditText)rootView.findViewById(R.id.confirm_newPW);

        return rootView;
    }

    private void account_delete() {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Cancel",null)
                .setNegativeButton("Admit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delaccount.DeleteAccount_Asycn(usn, pw);
                        Log.e("checkDelete","check");
                    }
                })
                .setCancelable(false)
                .show();

    }


    public void account_updatePW() {
        if(newPW.getText().toString().equals(confirm_newPW.getText().toString()))   {
            //Toast.makeText(getContext(),"Password change Complete",Toast.LENGTH_SHORT).show();
            if (newPW.getText().toString().matches(".*[0-9].*") && newPW.getText().toString().matches(".*[a-z].*") && newPW.getText().toString().length() >= 7) {
                //Toast.makeText(getContext(),"Password change Complete",Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(getContext())
                        .setTitle("Update password")
                        .setMessage("Are you sure you want to update your password?")
                        .setPositiveButton("Cancel",null)
                        .setNegativeButton("Admit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                changepw.changepw_Asycn(usn, newPW.getText().toString());
                                Log.e("checkDelete","check");
                            }
                        })
                        .setCancelable(false)
                        .show();
            }   else    {
                Toast.makeText(getContext(), "Password rule is wrong", Toast.LENGTH_SHORT).show();
            }
        }   else    {
            Toast.makeText(getContext(), "Password is not match", Toast.LENGTH_SHORT).show();
            Log.e("PW confirm", newPW +"/"+ confirm_newPW);
        }

    }
}
