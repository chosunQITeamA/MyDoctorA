package com.example.khseob0715.sanfirst.navi_fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.ServerConn.SearchList;
import com.example.khseob0715.sanfirst.ServerConn.UserConnReq;
import com.example.khseob0715.sanfirst.ServerConn.showConnectList;

import static com.example.khseob0715.sanfirst.UserActivity.UserActivity.usn;


/**
 * A simple {@link Fragment} subclass.
 */

public class Fragment_SearchDoctor extends Fragment {

    private ListView listView, listView2, listView3, listView4;

    private myAdapter adapter;     // 연결 의사 리스트  // connect
    private myAdapter2 adapter2;   // 전체 의사 리스트  // search
    private myAdapter3 adapter3;   // 의사 수락 리스트  // acceptance
    private myAdapter4 adapter4;   // 내가 의사한테 요청해서 기다리는 리스트 // waiting

    private String[] items = new String[10];

    private ViewGroup rootView;

    int searchmethod = 0;

    private Handler handler;

    public static int search_count = 0;   // connection request btn
    public static String[] Search_fname = new String[100];
    public static String[] Search_lname = new String[100];
    public static String[] Search_ID = new String[100];
    public static String[] Search_Gender = new String[100];
    public static String[] Search_old = new String[100];
    public static String[] Search_usn = new String[100];

    public static int connection_count = 0;  // 연결된 얘들
    public static String[] Connect_fname = new String[100];
    public static String[] Connect_lname = new String[100];
    public static String[] Connect_ID = new String[100];
    public static String[] Connect_Phone = new String[100];
    public static String[] Connect_Gender = new String[100];
    public static String[] Connect_old = new String[100];
    public static String[] Connect_usn = new String[100];

    public static int waiting_count = 0;    // 요청한 애들
    public static String[] waiting_fname = new String[100];
    public static String[] waiting_lname = new String[100];
    public static String[] waiting_ID = new String[100];
    public static String[] waiting_Gender = new String[100];
    public static String[] waiting_old = new String[100];
    public static String[] waiting_usn = new String[100];

    public static int accepatance_count = 0;    // 요청받은 애들
    public static String[] accepatance_fname = new String[100];
    public static String[] accepatance_lname = new String[100];
    public static String[] accepatance_ID = new String[100];
    public static String[] accepatance_Phone = new String[100];
    public static String[] accepatance_Gender = new String[100];
    public static String[] accepatance_old = new String[100];
    public static String[] accepatance_usn = new String[100];

    private EditText searchName2, searchEmail2;

    private Button searchDoctorBtn, ALLBtn;
    private Button Disconnect, Connect, Acceptance, Waiting;

    SearchList searchlist = new SearchList();
    showConnectList showconlist = new showConnectList();

    UserConnReq conreq = new UserConnReq();

    Fragment_SearchDoctor mFsearchD_Context;

    public Fragment_SearchDoctor() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search_doctor, container, false);

        listView = (ListView) rootView.findViewById(R.id.DoctorList);
        adapter = new myAdapter();
        listView.setAdapter(adapter);

        listView2 = (ListView) rootView.findViewById(R.id.DoctorList2);
        adapter2 = new myAdapter2();
        listView2.setAdapter(adapter2);

        listView3 = (ListView) rootView.findViewById(R.id.DoctorRequestList);
        adapter3 = new myAdapter3();
        listView3.setAdapter(adapter3);

        listView4 = (ListView)rootView.findViewById(R.id.DoctorWaitingList);
        adapter4 = new myAdapter4();
        listView4.setAdapter(adapter4);

        searchName2 = (EditText) rootView.findViewById(R.id.SearchName2);
        searchEmail2 = (EditText) rootView.findViewById(R.id.SearchEmail2);
        searchEmail2.setVisibility(View.GONE);

        Spinner spinner2 = (Spinner) rootView.findViewById(R.id.spinner2);

        ArrayAdapter Spinner_adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.search, R.layout.spinner_item);
        spinner2.setAdapter(Spinner_adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position == 0) {
                    searchmethod = 0;
                    searchName2.setVisibility(View.VISIBLE);
                    searchEmail2.setVisibility(View.GONE);
                } else {
                    searchmethod = 1;
                    searchName2.setVisibility(View.GONE);
                    searchEmail2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        TabHost host = (TabHost) rootView.findViewById(R.id.DoctorTabHost);
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

        for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#000000"));
        }

        searchDoctorBtn = (Button) rootView.findViewById(R.id.SearchdisconDoctor);
        searchDoctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = null;
                String value = null;
                if (searchmethod == 0) {
                    type = "name";
                    value = searchName2.getText().toString();
                } else if (searchmethod == 1) {
                    type = "email";
                    value = searchEmail2.getText().toString();
                }
                searchlist.SearchList_Asycn(1, type, value);
                handler.postDelayed(new Update_list2(), 1000);

            }
        });

        ALLBtn = (Button) rootView.findViewById(R.id.AllDoctor);
        ALLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchlist.SearchList_Asycn(1);
                handler.postDelayed(new Update_list2(), 1000);
            }
        });

        handler = new Handler();

        showconlist.showConnectList_Asycn(1, usn);
        searchlist.SearchList_Asycn(1);

        handler.postDelayed(new Update_list1(), 1000);
        handler.postDelayed(new Update_list2(), 1000);
        handler.postDelayed(new Update_list3(), 1000);
        handler.postDelayed(new Update_list4(), 1000);

        mFsearchD_Context = this;

        return rootView;
    }

    // addr : 0=connectbyuserapp   1=connectbydoctorapp    2=conrequestbydoctorapp     3=conrequestbyuserapp   4=disconnectuserapp  //      conreq.ConnRequest_Asycn(addr, usernum, doctornnum);


    class myAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return connection_count;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            SearchDoctor view = new SearchDoctor(rootView.getContext());
            final LinearLayout detail_layout = (LinearLayout) view.findViewById(R.id.DetailLayout);

            Button Detail = (Button) view.findViewById(R.id.list1DetailBtn);
            Detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detail_layout.setVisibility(View.VISIBLE);
                }
            });

            Button Disconnect = (Button) view.findViewById(R.id.Disconnect);
            Disconnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String DSN = Connect_usn[position];//-------------------------------------------------------------------------------------------------------------
                    Toast.makeText(getContext(),DSN,Toast.LENGTH_SHORT).show();
                    conreq.ConnRequest_Asycn(1, usn, Integer.parseInt(DSN));
                    PopupDialog(2);
                }
            });

            TextView FirstName = (TextView) view.findViewById(R.id.List1FirstName);
            TextView LastName = (TextView) view.findViewById(R.id.List1LastName);
            TextView ID = (TextView) view.findViewById(R.id.List1DoctorEmailText);
            TextView Phone = (TextView) view.findViewById(R.id.List1DoctorPhoneText);
            TextView Gender = (TextView) view.findViewById(R.id.List1DoctorGenderText);
            TextView Old = (TextView) view.findViewById(R.id.List1DoctorOldText);

            FirstName.setText(Connect_fname[position]);
            LastName.setText(Connect_fname[position]);
            ID.setText(Connect_ID[position]);
            Phone.setText(Connect_Phone[position]);
            Gender.setText(Connect_Gender[position]);
            Old.setText(Connect_old[position]);

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
            return search_count;
        }

        @Override
        public Object getItem(int position) {
            return Search_fname[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            SearchDoctor2 view = new SearchDoctor2(rootView.getContext());

            final LinearLayout detail_layout2 = (LinearLayout) view.findViewById(R.id.DetailLayout2);

            Button Detail = (Button) view.findViewById(R.id.list2DetailBtn);
            Detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detail_layout2.setVisibility(View.VISIBLE);
                }
            });

            Button Connection = (Button) view.findViewById(R.id.Connect);
            Connection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String DSN = Search_usn[position];//-------------------------------------------------------------------------------------------------------------
                    Toast.makeText(getContext(),DSN,Toast.LENGTH_SHORT).show();
                    conreq.ConnRequest_Asycn(0, usn, Integer.parseInt(DSN));
                    PopupDialog(3);
                }
            });

            TextView FirstName = (TextView) view.findViewById(R.id.List2FirstName);
            TextView LastName = (TextView) view.findViewById(R.id.List2LastName);
            TextView DoctorEmail = (TextView) view.findViewById(R.id.List2DoctorEmailText);
            TextView Gender = (TextView) view.findViewById(R.id.List2DoctorGenderText);
            TextView Old = (TextView) view.findViewById(R.id.List2DoctorOldText);

            FirstName.setText(Search_fname[position]);
            LastName.setText(Search_lname[position]);
            DoctorEmail.setText(Search_ID[position]);
            Gender.setText(Search_Gender[position]);
            Old.setText(Search_old[position]);

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


    class myAdapter3 extends BaseAdapter { // 의사가 수락을 기다리는 것.
        // 의사가 나한테 요청한 것.
        @Override
        public int getCount() {
            return accepatance_count;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            SearchDoctor3 view = new SearchDoctor3(rootView.getContext());

            final LinearLayout detail_layout2 = (LinearLayout) view.findViewById(R.id.DetailLayout2);

            Button Detail = (Button) view.findViewById(R.id.list4DetailBtn);
            Detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detail_layout2.setVisibility(View.VISIBLE);
                }
            });

            Button Acceptance = (Button) view.findViewById(R.id.Waiting);
            Acceptance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String DSN = accepatance_usn[position];//-------------------------------------------------------------------------------------------------------------
                    Toast.makeText(getContext(),DSN,Toast.LENGTH_SHORT).show();
                    conreq.ConnRequest_Asycn(2, usn, Integer.parseInt(DSN));
                    PopupDialog(0);
                }
            });

            TextView FirstName = (TextView) view.findViewById(R.id.List4FirstName);
            TextView LastName = (TextView) view.findViewById(R.id.List4LastName);
            TextView DoctorEmail = (TextView) view.findViewById(R.id.List4DoctorEmailText);
            TextView Gender = (TextView) view.findViewById(R.id.List4DoctorGenderText);
            TextView Old = (TextView) view.findViewById(R.id.List4DoctorOldText);

            FirstName.setText(accepatance_fname[position]);
            LastName.setText(accepatance_lname[position]);
            DoctorEmail.setText(accepatance_ID[position]);
            Gender.setText(accepatance_Gender[position]);
            Old.setText(accepatance_old[position]);

            return view;
        }
    }

    class SearchDoctor3 extends LinearLayout {
        TextView Test;

        public SearchDoctor3(Context context) {
            super(context);
            init(context);
        }

        public SearchDoctor3(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        private void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.search_doctor_list4, this);
        }

    }


    class myAdapter4 extends BaseAdapter {

        @Override
        public int getCount() {
            return waiting_count;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            SearchDoctor4 view = new SearchDoctor4(rootView.getContext());

            final LinearLayout detail_layout2 = (LinearLayout) view.findViewById(R.id.DetailLayout2);

            Button Detail = (Button) view.findViewById(R.id.list3DetailBtn);
            Detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detail_layout2.setVisibility(View.VISIBLE);
                }
            });

            Button Waiting = (Button) view.findViewById(R.id.Acceptance);
            Waiting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String DSN = waiting_usn[position];//-------------------------------------------------------------------------------------------------------------
                    Toast.makeText(getContext(),DSN,Toast.LENGTH_SHORT).show();
                    conreq.ConnRequest_Asycn(2, usn, Integer.parseInt(DSN));
                    PopupDialog(1);
                }
            });

            TextView FirstName = (TextView) view.findViewById(R.id.List3FirstName);
            TextView LastName = (TextView) view.findViewById(R.id.List3LastName);
            TextView ID = (TextView) view.findViewById(R.id.List3DoctorEmailText);
            TextView Gender = (TextView) view.findViewById(R.id.List3DoctorGenderText);
            TextView Old = (TextView) view.findViewById(R.id.List3DoctorOldText);

            FirstName.setText(waiting_fname[position]);
            LastName.setText(waiting_fname[position]);
            ID.setText(waiting_ID[position]);
            Gender.setText(waiting_Gender[position]);
            Old.setText(waiting_old[position]);

            return view;
        }
    }

    class SearchDoctor4 extends LinearLayout {
        TextView Test;

        public SearchDoctor4(Context context) {
            super(context);
            init(context);
        }

        public SearchDoctor4(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        private void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.search_doctor_list3, this);
        }
    }


    private class Update_list1 implements Runnable {
        // 연결 의사 리스트 호출
        @Override
        public void run() {
            adapter.notifyDataSetChanged();
        }
    }

    private class Update_list2 implements Runnable {
        // 전체 의사 리스트 호출
        @Override
        public void run() {
            adapter2.notifyDataSetChanged();
        }
    }

    private class Update_list3 implements Runnable {
        //  연결 요청 리스트
        @Override
        public void run() {
            adapter3.notifyDataSetChanged();
        }
    }

    private class Update_list4 implements Runnable {
        //  내가 기다리는 리스트
        @Override
        public void run() {
            adapter4.notifyDataSetChanged();
        }
    }

    public void PopupDialog(int state) {
        String title = null;
        String text = null;

        switch (state)  {
            case 0 :    // click Waiting
                title = "Waiting!!";
                text = "Waiting for Accaptance";
                break;
            case 1 :    // click Waiting
                title = "Completed!";
                text = "Accaptance is Completed!";
                break;
            case 2 :    // click disconnection
                title = "Completed!!";
                text = "Disconnection Completed!!";
                break;
            case 3 :    // click disconnection
                title = "Completed!!";
                text = "Connection request is  Completed!!";
                break;
        }

        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(text)
                .setNegativeButton("Admit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        handler = new Handler();
                        showconlist.showConnectList_Asycn(1, usn);
                        searchlist.SearchList_Asycn(1);
                        handler.postDelayed(new Update_list1(), 1000);
                        handler.postDelayed(new Update_list2(), 1000);
                        handler.postDelayed(new Update_list3(), 1000);
                        handler.postDelayed(new Update_list4(), 1000);
                    }
                })
                .show();
        //
    }
}
