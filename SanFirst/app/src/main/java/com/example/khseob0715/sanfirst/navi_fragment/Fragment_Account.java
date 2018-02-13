package com.example.khseob0715.sanfirst.navi_fragment;


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
        delaccount.DeleteAccount_Asycn(usn, pw);
    }


    public void account_updatePW() {
        if(newPW.getText().toString().equals(confirm_newPW.getText().toString()))   {
            Toast.makeText(getContext(),"complete",Toast.LENGTH_SHORT).show();
            changepw.changepw_Asycn(usn, newPW.getText().toString());
        }   else    {
            Toast.makeText(getContext(), "Password is different", Toast.LENGTH_SHORT).show();
            Log.e("PW confirm", newPW +"/"+ confirm_newPW);
        }

    }
}
