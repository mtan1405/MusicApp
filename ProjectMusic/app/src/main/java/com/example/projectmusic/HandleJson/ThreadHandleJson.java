package com.example.projectmusic.HandleJson;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;



import com.example.projectmusic.PlayListManager.PlayListMusic.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ThreadHandleJson extends AsyncTask <String ,Song , String > {
    private final String BASE_URL = "https://shazam.p.rapidapi.com/" ;
    private String request = null ;
    public static final int SEARCH = 5;
    public static final int LIST_RECOMMENDATION = 14 ;
    public static final int STOP_THREAD = 0 ;
    public static final int AVAILABLE_LIST = 124;
    public CallBack callBack  ;
    private int currentTask  = STOP_THREAD;
    public Setup setup ;

    ThreadHandleJson (String getList  , int selection )
    {
        switch (selection)
        {
            case SEARCH:
                request = getURLForSearch (getList) ;
                break;
            case LIST_RECOMMENDATION:
                request =getURLForRcm (getList) ;
                break;
        }
    }
    ThreadHandleJson ()
    {
        this (null , ThreadHandleJson.LIST_RECOMMENDATION) ;

    }
    public void chooseQuest (String request){
       this.request = getURLForSearch(request);
    }
    public  void chooseRcm (){
        this.request = getURLForRcm(null);
    }



    private String getURLForRcm(String getList) {
        Log.i ("String rcm " ,BASE_URL + "songs/list-artist-top-tracks?id=40008598&locale=en-US"  ) ;
        return BASE_URL + "songs/list-artist-top-tracks?id=40008598&locale=en-US";
    }

    private String getURLForSearch(String getList) {
        String path [] = getList.split(" ") ;
        StringBuilder stringBuilder = new StringBuilder(BASE_URL +"search?term=") ;
        for (String s : path)
        {
            stringBuilder.append(s+"%20") ;
        }
        stringBuilder.append("&locale=en-US&offset=0&limit=5") ;
        Log.i ("StringForSearch" , stringBuilder.toString() );
        return stringBuilder.toString();
    }
    public String  requestJsonFromAPI () throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(this.request)
                .get()
                .addHeader("X-RapidAPI-Key", "7b9c9d3d1amshf78158a2603a9c4p1fb338jsn5e3370060d7e")
                .addHeader("X-RapidAPI-Host", "shazam.p.rapidapi.com")
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();

    }
   public List <Song> getListOfSongInRecommend (String string ) throws JSONException {
       JSONObject jsonObject = new JSONObject(string);
       List<Song> songList= new ArrayList<>() ;
        JSONArray jsonArray = jsonObject.getJSONArray("tracks") ;
        for (int i = 0 ; i < jsonArray.length() ; i++ ) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i) ;
            JSONObject jsonHub = jsonObject1.getJSONObject("hub") ;
            JSONArray jsonAction = jsonHub.getJSONArray("actions") ;
            JSONObject jsonLinkMu = jsonAction.getJSONObject(1) ;
            String linkSong = jsonLinkMu.getString("uri");
            JSONObject jsonImage = jsonObject1.getJSONObject("images") ;
            String linkImag = jsonImage.getString("coverart") ;
            String name = jsonObject1.getString("title");
            songList.add(new Song(linkImag,linkSong,name , jsonObject1.getString("subtitle") )) ;

            //Log.i("link" , link); // get String URL
            Log.i ("get LinkSong" , linkSong) ;
        }
        return songList;
    }
    public  List<Song> getListOfSongInQuest (String string )throws  JSONException {
        List<Song> songList = new ArrayList<>(10) ;
        JSONObject jsonRoot = new JSONObject(string) ;
        JSONObject jsonTracks  = jsonRoot.getJSONObject("tracks") ;
        JSONArray jsonArray  = jsonTracks.getJSONArray("hits") ;
        for (int i = 0  ; i< jsonArray.length() ; i++ ){

            JSONObject jsonObject = jsonArray.getJSONObject(i) ;
            JSONObject jsonTrack = jsonObject.getJSONObject("track") ;
            JSONObject jsonImage = jsonTrack.getJSONObject("images") ;
            JSONObject jsonHub = jsonTrack.getJSONObject("hub") ;
            JSONArray jsonAction = jsonHub.getJSONArray("actions") ;
            JSONObject jsonSongLink = jsonAction.getJSONObject(1) ;

            songList.add (new Song (jsonImage.getString("coverart")  , jsonSongLink.getString("uri") , jsonTrack.getString("title") , jsonTrack.getString("subtitle"))  ) ;
        }
        return songList ;
    }

    public void getBitmap (Song song){
       String src = song.getLinkImage();
        try {

            URL url = new URL(src);
            song. image = BitmapFactory.decodeStream(url.openConnection().getInputStream()) ;
            publishProgress(new Song[]{song});
        }
        catch (Exception e ){
            Log.i("Bitmap" , "Eroo") ;

        }
    }

    @SuppressLint("SuspiciousIndentation")
    public void setCurrentTask(int currentTask) {
        if (currentTask==SEARCH || currentTask == LIST_RECOMMENDATION || currentTask == STOP_THREAD||currentTask==AVAILABLE_LIST)
        this.currentTask = currentTask;
    }
    public int getCurrentTask () {
        return currentTask;
    }

    public  void progress (Song song) {
       this.publishProgress(song);

    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            switch (currentTask){
                case LIST_RECOMMENDATION:
                    setup.setUpForRecommend();
                    break;
                case SEARCH:
                    setup.setUpForQuest();
                    break;
                case AVAILABLE_LIST:
                    setup.setupForAvailableList();
            }

        }
        catch (Exception e){
            Log.i ("e" , e.getMessage());
        }



        return  null ;
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled();
        currentTask = STOP_THREAD ;

    }


    @Override
    protected void onProgressUpdate(Song... values) {

       callBack.setLoad(values[0]);
       super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        currentTask = STOP_THREAD ;
        super.onPostExecute(s);
    }

    public interface CallBack {
       void getSongList (List<Song> songList) ;
       void setLoad (Song song) ;
    }

    public interface Setup{
       void setUpForQuest () throws Exception ;
       void setUpForRecommend () throws Exception ;
       void setupForAvailableList () throws Exception ;

    }

    }



