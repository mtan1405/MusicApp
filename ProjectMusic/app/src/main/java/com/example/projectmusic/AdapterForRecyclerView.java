package com.example.projectmusic;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmusic.DBForMusic.DBManager;
import com.example.projectmusic.PlayListManager.PlayListManager;
import com.example.projectmusic.PlayListManager.PlayListMusic.Song;

import java.util.List;

public class AdapterForRecyclerView extends RecyclerView.Adapter <AdapterForRecyclerView.ViewHolder>  {
    List<Song> list ;
    ClickView clickView ;
    Song currentSong = new Song("asdf" , "d" , "s" ,"ds") ;
    DBManager dbManager ;

    DisableView disableView ;



    BroadcastReceiver broadcastReceiver ;
    public AdapterForRecyclerView ( @NonNull   List <Song> list , Context context) {
        this.list = list ;
         broadcastReceiver  = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                   AdapterForRecyclerView.this.notifyDataSetChanged();
            }
        } ;
         dbManager = DBManager.getInstance();
         IntentFilter intentFilter = new IntentFilter();
         intentFilter.addAction(PlayListManager.PREPARED) ;
        context.registerReceiver(broadcastReceiver,intentFilter) ;

    }

    public void setClickView(ClickView clickView) {
        this.clickView = clickView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater  = LayoutInflater.from(parent.getContext());
        View view =    inflater.inflate(R.layout.element_detail , null) ;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (disableView != null){
            disableView.disable(holder.itemView);
        }
        setDataToView   (holder.itemView , position);

        setBackgroundForChoosenItem(holder.itemView , position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void setDataToView (@NonNull View view  , int pos ){

      ImageView view1 =  view.findViewById(R.id.imag_song) ;

      view1.setImageBitmap(list.get(pos).image);
      TextView textView = view.findViewById(R.id.song_name) ;
      textView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              clickSong(pos , null);

          }
      });


      textView.setText(list.get(pos).getName());
      TextView textViewAuthor = view.findViewById(R.id.author) ;
      textViewAuthor.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              clickSong(pos , null);
          }
      });
      textViewAuthor.setText(list.get(pos).getAuthor());
      view1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              clickSong(pos , clickView);
          }
      });
        Button button = view.findViewById(R.id.add_song) ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickView != null)
                    clickView.addSong(list.get(pos)) ;
            }
        });
        Button deleteBtn  =  view.findViewById(R.id.remove) ;
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickView != null){
                    clickView.removeSong(list.get(pos));
                }
            }
        });
    }
    private void clickSong (int position , ClickView clickView ){
        if (currentSong != list.get(position)){
            currentSong = list.get(position);
            PlayListManager.getPlayListManager().chooseSong(position);
        }


        if (clickView != null){
            clickView.clickImageSong();
        }
    }

    private void setBackgroundForChoosenItem(View view , int pos) {
        if (PlayListManager.getPlayListManager().getCurrentSong()!= null)
        {
//        if ( PlayListManager.getPlayListManager().getCurrentSong().getLinkSong() == list.get(pos).getLinkSong() && pos == PlayListManager.getPlayListManager().getCurrentIndexSong() ){
            if ( PlayListManager.getPlayListManager().getCurrentSong().getLinkSong() == list.get(pos).getLinkSong()  ){
            view.findViewById(R.id.element_layout).setBackgroundColor(view.getContext().getColor(R.color.teal_200)) ;
        }else {
            view.findViewById(R.id.element_layout).setBackgroundColor(view.getContext().getColor(R.color.white));
        }

        }else {
            view.findViewById(R.id.element_layout).setBackgroundColor(view.getContext().getColor(R.color.white));
        }


    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
    public interface ClickView {
        void clickImageSong() ;
        void addSong (Song song ) ;
        void removeSong ( Song song) ;

    }
    public interface DisableView {
        void disable (View view ) ;
    }



}
