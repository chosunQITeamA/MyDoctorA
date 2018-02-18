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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.ServerConn.SearchList;

/**
 * A simple {@link Fragment} subclass.
 */
public class D_Fragment_usersearch extends Fragment {

    private ListView listView;

    private myAdapter adapter;

    private String[] items = new String[10];

    private ViewGroup rootView;

    private EditText searchName, searchEmail;

    private Button searchUserBtn;

    private int searchmethod = 0;

    SearchList searchlist = new SearchList();

    public D_Fragment_usersearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup)inflater.inflate(R.layout.d__fragment_usersearch, container, false);

        listView = (ListView) rootView.findViewById(R.id.UserSearchList);
        adapter = new myAdapter();
        listView.setAdapter(adapter);

        searchName = (EditText)rootView.findViewById(R.id.PtSearchName2);
        searchEmail = (EditText)rootView.findViewById(R.id.PtSearchEmail2);
        searchEmail.setVisibility(View.GONE);

        Spinner spinner = (Spinner)rootView.findViewById(R.id.spinner2);

        ArrayAdapter Spinner_adapter = ArrayAdapter.createFromResource(getContext(), R.array.search, R.layout.spinner_item);
        spinner.setAdapter(Spinner_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l){

                if(position == 0){
                    searchmethod = 0;
                    searchName.setVisibility(View.VISIBLE);
                    searchEmail.setVisibility(View.GONE);
                }else{
                    searchmethod = 1;
                    searchName.setVisibility(View.GONE);
                    searchEmail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchUserBtn = (Button) rootView.findViewById(R.id.SearchUserBtn);
        searchUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = null;
                String value = null;
                if(searchmethod == 0)    {
                    type = "name";
                    value = searchName.getText().toString();
                }   else if (searchmethod == 1) {
                    type = "email";
                    value = searchEmail.getText().toString();
                }
                searchlist.SearchList_Asycn(0, type, value);
            }
        });
        searchlist.SearchList_Asycn(0);

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
            SearchDoctor view = new SearchDoctor(rootView.getContext());

            return view;
        }
    }

    class SearchDoctor extends LinearLayout {
        TextView Test;

        public SearchDoctor(Context context) {
            super(context);
            init(context);
        }

        public SearchDoctor(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        private void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.search_doctor_list, this);
        }

    }
}
