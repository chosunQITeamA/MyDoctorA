package com.example.khseob0715.sanfirst.navi_fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.ServerConn.ChangeProfile;

import static com.example.khseob0715.sanfirst.UserActivity.UserActivity.UserActContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Profile extends Fragment {

    private Button BirthSelectBtn, updateBtn;
    private DatePickerDialog datePickerDialog;
    private TextView email;
    private EditText Fname, Lname, Phone1, Phone2, Phone3;
    private RadioButton maleRadio, femaleRadio;

    private String S_Fname, S_Lname, S_Phone, S_Bitrh;

    ChangeProfile changeprofile = new ChangeProfile();

    int usn = 0;
    private String Semail, SFname, SLname, SPhone, SBirth;
    String pw = null;

    public Fragment_Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        usn = getArguments().getInt("usn");
        Semail = getArguments().getString("email");
        SFname = getArguments().getString("fname");
        SLname = getArguments().getString("lname");
        SPhone = getArguments().getString("phone");
        SBirth = getArguments().getString("birth");

        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);

        email = (TextView) rootView.findViewById(R.id.Email_editText);
        Fname = (EditText) rootView.findViewById(R.id.Fname_editText);
        Lname = (EditText) rootView.findViewById(R.id.Lname_editText);
        Phone1 = (EditText) rootView.findViewById(R.id.phone_editText1);
        Phone2 = (EditText) rootView.findViewById(R.id.phone_editText2);
        Phone3 = (EditText) rootView.findViewById(R.id.phone_editText3);
        BirthSelectBtn = (Button) rootView.findViewById(R.id.Birth_select);

        updateBtn = (Button) rootView.findViewById(R.id.update_profile_btn);

        email.setText(Semail);
        Fname.setText(SFname);
        Lname.setText(SLname);
        Phone1.setText(SPhone.substring(0,3));
        Phone2.setText(SPhone.substring(4,7));
        Phone3.setText(SPhone.substring(8,12));
        BirthSelectBtn.setText(SBirth);

        BirthSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        BirthSelectBtn.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                };

                datePickerDialog = new DatePickerDialog(view.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert, callback, 2018, 0, 19);
                datePickerDialog.show();
            }
        });

        updateBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                S_Fname = Fname.getText().toString();
                S_Lname = Lname.getText().toString();
                S_Phone = Phone1.getText().toString() + "-" + Phone2.getText().toString() + "-" + Phone3.getText().toString();
                S_Bitrh = BirthSelectBtn.getText().toString();

                if(S_Fname != null && S_Lname != null && S_Phone != null && S_Bitrh != null) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Update Profile")
                            .setMessage("Are you sure you want to update your profile?")
                            .setPositiveButton("Cancel",null)
                            .setNegativeButton("Admit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    changeprofile.changeprofile_Asycn(usn, S_Fname, S_Lname, S_Bitrh, S_Phone);
                                    Log.e("checkDelete","check");

                                    Fragment fragment = null;
                                    fragment = new Fragment_TabMain();

                                    FragmentTransaction ft = UserActContext.getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.content_fragment_layout, fragment);
                                    ft.commit();
                                }
                            })
                            .setCancelable(false)
                            .show();

                }   else    {
                    Toast.makeText(getActivity(), "Please fill the blank", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }
}
