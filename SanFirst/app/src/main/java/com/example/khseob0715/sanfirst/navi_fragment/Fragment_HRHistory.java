package com.example.khseob0715.sanfirst.navi_fragment;


import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.khseob0715.sanfirst.Activity.UserMainActivity;
import com.example.khseob0715.sanfirst.R;
import com.lylc.widget.circularprogressbar.CircularProgressBar;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_HRHistory extends Fragment {

    ListView listView;
    myAdapter adapter;

    String[] items = {"ss","das"};
    ViewGroup rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       rootView = (ViewGroup)inflater.inflate(R.layout.fragment_heartrate_history, container, false);

        listView = (ListView)rootView.findViewById(R.id.listView);
        adapter = new myAdapter();
        listView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }
    class myAdapter extends BaseAdapter{

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
            TextView view = new TextView(rootView.getContext());
            view.setText(items[position]);
            view.setTextSize(40.0f);
            return view;
        }
    }
}
