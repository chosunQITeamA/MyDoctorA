package com.example.khseob0715.sanfirst.navi_fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
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
import android.widget.TabHost;
import android.widget.TextView;

import com.example.khseob0715.sanfirst.R;


/**
 * A simple {@link Fragment} subclass.
 */

public class Fragment_SearchDoctor extends Fragment {

    private ListView listView, listView2;

    private myAdapter adapter;
    private myAdapter2 adapter2;

    private String[] items = new String[10];

    private ViewGroup rootView;

    private EditText searchName, searchEmail;
    private EditText searchName2, searchEmail2;

    public Fragment_SearchDoctor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_search_doctor, container, false);

        listView = (ListView) rootView.findViewById(R.id.DoctorList);
        adapter = new myAdapter();
        listView.setAdapter(adapter);

        listView2 = (ListView)rootView.findViewById(R.id.DoctorList2);
        adapter2 = new myAdapter2();
        listView2.setAdapter(adapter2);


        searchName = (EditText)rootView.findViewById(R.id.SearchName);
        searchEmail = (EditText)rootView.findViewById(R.id.SearchEmail);
        searchEmail.setVisibility(View.GONE);

        searchName2 = (EditText)rootView.findViewById(R.id.SearchName2);
        searchEmail2 = (EditText)rootView.findViewById(R.id.SearchEmail2);
        searchEmail2.setVisibility(View.GONE);

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

        Spinner spinner2 = (Spinner)rootView.findViewById(R.id.spinner2);

        ArrayAdapter Spinner_adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.search, R.layout.spinner_item);
        spinner2.setAdapter(Spinner_adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l){

                if(position == 0){
                    searchName2.setVisibility(View.VISIBLE);
                    searchEmail2.setVisibility(View.GONE);
                }else{
                    searchName2.setVisibility(View.GONE);
                    searchEmail2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        TabHost host = (TabHost)rootView.findViewById(R.id.DoctorTabHost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("tab1");
        //spec.setIndicator("My Doctor");
        spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.doctortext, null));
        spec.setContent(R.id.tab1);
        host.addTab(spec);

        spec = host.newTabSpec("tab2");
        //spec.setIndicator("Search");
        spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.searchtext, null));
        spec.setContent(R.id.tab2);
        host.addTab(spec);

        for(int i=0;i<host.getTabWidget().getChildCount();i++){
            TextView tv = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#000000"));
        }

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


    class myAdapter2 extends BaseAdapter {

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
            SearchDoctor2 view = new SearchDoctor2(rootView.getContext());

            return view;
        }
    }

    class SearchDoctor2 extends LinearLayout {
        TextView Test;

        public SearchDoctor2(Context context) {
            super(context);
            init(context);
        }

        public SearchDoctor2(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        private void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.search_doctor_list2, this);
        }

    }


}
