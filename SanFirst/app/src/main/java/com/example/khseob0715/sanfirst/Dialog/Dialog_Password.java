package com.example.khseob0715.sanfirst.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.TextView;

import com.example.khseob0715.sanfirst.R;

/**
 * Created by Aiden on 2018-02-11.
 */

public class Dialog_Password extends Dialog {
    private static final int LAYOUT = R.layout.dialog_password;

    public Dialog_Password(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
    }

}
