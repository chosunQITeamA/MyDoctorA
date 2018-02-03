package com.example.khseob0715.sanfirst.navi_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.khseob0715.sanfirst.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Account extends Fragment {

    public Fragment_Account() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_account, container, false);

        Button deleteBtn = (Button)rootView.findViewById(R.id.idDeleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(),"complete",Toast.LENGTH_SHORT).show();
            }
        });

        Button updateBtn = (Button)rootView.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"complete",Toast.LENGTH_SHORT).show();
            }
        });



        return rootView;
    }

}
