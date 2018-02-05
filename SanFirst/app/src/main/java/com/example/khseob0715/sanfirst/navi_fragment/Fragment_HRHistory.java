package com.example.khseob0715.sanfirst.navi_fragment;


import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.khseob0715.sanfirst.Activity.UserMainActivity;
import com.example.khseob0715.sanfirst.R;
import com.lylc.widget.circularprogressbar.CircularProgressBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_HRHistory extends Fragment {

    public int receivenowhrvalue =0;
    public TextView hrhistory;

    public CircularProgressBar hrseekbar;
    int hrseekstartval = 0;
    int hrseekendval = 0;

    UserMainActivity mainclass = new UserMainActivity();
    public Fragment_HRHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_heartrate_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Find 시작
        hrhistory = (TextView)view.findViewById(R.id.hrhistorytext);
        hrseekbar = (CircularProgressBar)view.findViewById(R.id.hearthistoryseekbar);

        startSubThread();
    }

    public void startSubThread()
    {
        //작업스레드 생성(매듭 묶는과정)
        MyRunnable myRunnable = new MyRunnable();
        Thread heartThread = new Thread(myRunnable);
        heartThread.setDaemon(true);
        heartThread.start();
    }

    android.os.Handler receivehearthandler = new android.os.Handler()
    {
        public void handleMessage(Message msg)
        {
            if (msg.what == 0)
            {
                hrseekstartval = hrseekendval;
                hrseekendval = mainclass.getHeartratevalue();
                heartseekani(hrseekstartval, hrseekendval);
                hrhistory.setText(String.valueOf(mainclass.getHeartratevalue()));
            }
        };
    };


    public class MyRunnable implements Runnable
    {
        @Override
        public void run()
        {
            while(true)
            {
                Message msg = Message.obtain();
                msg.what = 0;
                receivehearthandler.sendMessage(msg);
                try
                {
                    Thread.sleep(1000);
                }
                catch (Exception e)
                {
                }
            }
        }
    }

    // Heartrate Seekbar animation (startval, endval)
    public void heartseekani(int startval, int endval) {

        hrseekbar.animateProgressTo(startval, endval, new CircularProgressBar.ProgressAnimationListener() {
            @Override
            public void onAnimationStart() {
            }
            @Override
            public void onAnimationProgress(int progress) {
            }
            @Override
            public void onAnimationFinish() {
            }
        });

        hrseekstartval = endval;
    }
}
