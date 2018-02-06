package com.example.khseob0715.sanfirst.Activity;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.khseob0715.sanfirst.R;

/**
 * Created by Aiden on 2018-02-06.
 */

public class HistoricalHeart extends LinearLayout{
    TextView Test;

    public HistoricalHeart(Context context) {
        super(context);
        init(context);
    }

    public HistoricalHeart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.historical_heart_list,this);

        Test = (TextView)findViewById(R.id.Time);
    }


}
