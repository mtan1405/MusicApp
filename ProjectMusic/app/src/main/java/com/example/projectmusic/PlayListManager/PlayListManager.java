package com.example.projectmusic.PlayListManager;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.example.projectmusic.Application2;
import com.example.projectmusic.DBForMusic.DBManager;
import com.example.projectmusic.MusicService;
import com.example.projectmusic.PlayListManager.PlayListMusic.Song;

import java.util.ArrayList;
import java.util.List;

public class PlayListManager  {
    public static final String PREPARED = "afsadfasasdfasdfdfasdf" ;
 //   public static final String LINK = "afsadfasassdafasdfdfasdfdfasdf" ;
    private MediaPlayer mediaPlayer ;
    public List<Song>  searchedList ;
    private List<Song>   myList ;
    public List<Song>  currentList ;
    private Song currentSong = null;
    private int currentIndexSong = -1  ;

    private PlayListManager () {}
    private static PlayListManager playListManager ;
    private  void getMyList () {
        myList = new ArrayList<>() ;
    }
    public void chooseMyList () {
        currentList.clear();
        currentList.addAll(myList);
    }
    public  void chooseSearchList (){currentList = searchedList ;}


    public static  PlayListManager getPlayListManager() {
        if (playListManager == null){
            playListManager = new PlayListManager() ;
            playListManager.searchedList = new ArrayList<>() ;
            playListManager.getMyList();
           // playListManager.chooseMyList() ;
            playListManager.myList = DBManager.getInstance().getAllSong();

        }
        return playListManager;
    }
    public void chooseSong (int index ){
        if (mediaPlayer == null ) Application2.getContext().startService(new Intent(Application2.getContext() , MusicService.class));
        index = Math.abs(index) ;
        if (index >= currentList.size()){
            currentIndexSong =-1 ;
            return;
        }else if (mediaPlayer != null){

            currentIndexSong = index ;
            currentSong = currentList.get(index);
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(currentList.get(index).getLinkSong());
                mediaPlayer.prepareAsync();
            }
         catch (Exception e){
                Log.i ("setDataSoureError" , e.getMessage()) ;
         }
        }
    }

    public void setupMediaPlayer (){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                int a = PlayListManager.this.currentIndexSong+ 1 ;
                chooseSong(a);
                if (currentIndexSong <0 )
                    Application2.getContext().sendBroadcast(new Intent().setAction(PREPARED));

            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {

                return true ;
            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                Intent intent = new Intent().setAction(PREPARED) ;
                Application2.getContext().sendBroadcast(new Intent().setAction(PREPARED));
            }
        });
    }
    public void setMediaPlayer(MediaPlayer mediaPlayer){
        this.mediaPlayer = mediaPlayer;
    }
    public void seekTo (int mls){
        if (mediaPlayer != null ){
            mediaPlayer.seekTo(mls);
        }

    }
    public int getDuration (){
        return mediaPlayer==null ? 0: mediaPlayer.getDuration();
    }
    public int getCurrentPosition (){
        return mediaPlayer==null?0:mediaPlayer.getCurrentPosition();
    }

    public int getCurrentIndexSong() {
        return currentIndexSong;
    }
    public void mediaPause () {
        if (mediaPlayer!= null) {
            mediaPlayer.pause();
        }
    }
    public void mediaStart (){
        if (mediaPlayer != null )mediaPlayer.start();
    }
    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }
    public Song getCurrentSong (){
       return currentSong;
    }
    public void start (){
        if (mediaPlayer!= null) mediaPlayer.start() ;
    }
    public  void pause () {
        if (mediaPlayer!= null) mediaPlayer.pause(); ;
    }
    public boolean isPlaying (){
      return   mediaPlayer==null?false:mediaPlayer.isPlaying();
    }

    public void addMyList (Song song) {
        try {
            DBManager.getInstance().insert(song);
            myList.add(song);
        }catch (Exception e ){
            Toast.makeText(Application2.getContext(),"Đã có "+ song.getName() +" rồi ", Toast.LENGTH_SHORT).show();
        }
    }
    public void removeMyList (Song song ) {
        DBManager.getInstance().delete(song) ;
        myList.remove(song);
    }



}
