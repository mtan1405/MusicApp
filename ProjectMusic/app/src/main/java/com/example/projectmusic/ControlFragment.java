package com.example.projectmusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.projectmusic.PlayListManager.PlayListManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControlFragment  extends  Fragment {
    PlayListManager playListManager;
    SeekBar seekBar ;
    TextView textView ;
    BroadcastReceiver broadcastReceiver ;
    Button nextButton ;
    Button preButton ;
    Button playButton ;

    public ControlFragment() {
        // Required empty public constructor
    }
    Context context ;

    public static ControlFragment newInstance() {
        ControlFragment fragment = new ControlFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playListManager = PlayListManager.getPlayListManager();
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_control, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView(view);
    }
    void setUpView (View view){
        textView = view.findViewById(R.id.textView2) ;
        seekBar = view.findViewById(R.id.seekbar)  ;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    playListManager.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Handler handler = new Handler() ;
        handler.post(new Runnable() {
            @Override
            public void run() {
               seekBar.setProgress(playListManager.getCurrentPosition());
               handler.postDelayed(this , 100 ) ;

            }
        } ) ;
        seekBar.setMax(PlayListManager.getPlayListManager().getDuration());

        broadcastReceiver = new Receiver() ;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PlayListManager.PREPARED);
        getContext().registerReceiver(broadcastReceiver , intentFilter);
        nextButton = view.findViewById(R.id.next_button);
        preButton = view .findViewById(R.id.pre_button) ;
        playButton = view.findViewById(R.id.play_button) ;
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayListManager.getPlayListManager().chooseSong(PlayListManager.getPlayListManager().getCurrentIndexSong()+1);
            }
        });
        preButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayListManager.getPlayListManager().chooseSong(PlayListManager.getPlayListManager().getCurrentIndexSong()-1);
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PlayListManager.getPlayListManager().isPlaying()){
                    playListManager.pause();
                } else {
                    playListManager.start();
                }
                updatePlayButton(view.getContext());

            }
        });
        updatePlayButton( view.getContext());



    }
   private void updatePlayButton (Context context){
       if (PlayListManager.getPlayListManager().isPlaying()) {
           Resources res = context.getResources();
           Drawable myImage = ResourcesCompat.getDrawable(res, R.drawable.play_arrow, null);
           playButton.setBackground(myImage);
       }else {
           Resources res = context.getResources();
           Drawable myImage = ResourcesCompat.getDrawable(res, R.drawable.stop_arrow, null);
           playButton.setBackground(myImage);
       }
    }


    void updateUI (Context context) {

        if (seekBar != null ) {
            seekBar.setMax(playListManager.getDuration()) ;
        }
        if (getView() != null){
            TextView textView1 = getView().findViewById(R.id.textView2) ;
            textView1.setText(PlayListManager.getPlayListManager().getCurrentSong().getName()+"-"+PlayListManager.getPlayListManager().getCurrentSong().getAuthor());


        }
        updatePlayButton(context);

    }
    public class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            updateUI (context) ;

        }
    }



}