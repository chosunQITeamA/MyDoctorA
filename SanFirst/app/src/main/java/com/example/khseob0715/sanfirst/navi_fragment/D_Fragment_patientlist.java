package com.example.khseob0715.sanfirst.navi_fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.ServerConn.showConnectList;

import static com.example.khseob0715.sanfirst.UserActivity.UserActivity.usn;

/**
 * A simple {@link Fragment} subclass.
 */
public class D_Fragment_patientlist extends Fragment {

    private ListView listView;

    private myAdapter adapter;

    private ViewGroup rootView;

    private String[] items = new String[10];

    private EditText searchName, searchEmail;

    showConnectList showconlist = new showConnectList();

    public D_Fragment_patientlist() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup)inflater.inflate(R.layout.d__fragment_patient__list, container, false);

        listView = (ListView) rootView.findViewById(R.id.PatienList);
        adapter = new myAdapter();
        listView.setAdapter(adapter);

        searchName = (EditText)rootView.findViewById(R.id.PtSearchName);
        searchEmail = (EditText)rootView.findViewById(R.id.PtSearchEmail);
        searchEmail.setVisibility(View.GONE);

        Spinner spinner = (Spinner)rootView.findViewById(R.id.spinner);

        ArrayAdapter Spinner_adapter = ArrayAdapter.createFromResource(getContext(), R.array.search, R.layout.spinner_item);
        spinner.setAdapter(Spinner_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l){

                if(position == 0){
                    searchName.setVisibility(View.VISIBLE);
                    searchEmail.setVisibility(View.GONE);
                }else{
                    searchName.setVisibility(View.GONE);
                    searchEmail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        showconlist.showConnectList_Asycn(0, usn);

        return rootView;
    }

    class myAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PatientList view = new PatientList(rootView.getContext());
            return view;
        }
    }

    class PatientList extends LinearLayout {
        TextView Test;

        public PatientList(Context context) {
            super(context);
            init(context);
        }

        public PatientList(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        private void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.search_doctor_list2, this);
        }

    }
}
