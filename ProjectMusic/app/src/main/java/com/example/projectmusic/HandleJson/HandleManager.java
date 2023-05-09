package com.example.projectmusic.HandleJson;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.projectmusic.MainActivity;
import com.example.projectmusic.PlayListManager.PlayListMusic.Song;

import java.util.List;

public class HandleManager {
    private  ThreadHandleJson threadHandleJson ;
    private static HandleManager handleManager  ;
    public List<Song> list ;
    private int selection = ThreadHandleJson.LIST_RECOMMENDATION ;

    private ThreadHandleJson.Setup setup ;
    private String keyWord = null ;
    private HandleManager.Callback callbackOfManager ;
    private ThreadHandleJson.CallBack callbackOfThread ;

    private HandleManager () {

        threadHandleJson = new ThreadHandleJson( );
        delayThread = new DelayThread();
    }
    private HandleManager( HandleManager handleManager ) {}
    private DelayThread delayThread ;
    public static HandleManager getInstance( List<Song> songList , HandleManager.Callback callBack  , @NonNull Callback callbackOfManager ) {
        if (handleManager == null ) {
            handleManager= new HandleManager() ;
            handleManager.setSetupDataInThread () ;
            handleManager.setCallbackOfManager(callbackOfManager);
        }
        handleManager.list = songList ;
        return handleManager;
    }
    private void setCallbackDataInThread (Callback loadUI) {
        threadHandleJson.callBack = new ThreadHandleJson.CallBack() {
            @Override
            public void getSongList(List<Song> songList) {
                list.clear();
                list.addAll(songList) ;
            }

            @Override
            public void setLoad(Song song) {
                loadUI.loadUI(song);

            }
        };
    }
    private void setSetupDataInThread (){
        threadHandleJson.setup = new ThreadHandleJson.Setup() {
            @Override
            public void setUpForQuest() throws Exception {
                threadHandleJson.setCurrentTask(ThreadHandleJson.SEARCH);
                threadHandleJson.chooseQuest(keyWord);
                String jsonBody = threadHandleJson.requestJsonFromAPI();
                threadHandleJson.callBack.getSongList(threadHandleJson.getListOfSongInQuest(jsonBody));
                for (Song song :list){
                    if (threadHandleJson.isCancelled() ) {break;}
                    threadHandleJson.getBitmap(song);
                }


            }

            @Override
            public void setUpForRecommend() throws Exception {
                threadHandleJson.setCurrentTask(ThreadHandleJson.LIST_RECOMMENDATION);
                threadHandleJson.chooseRcm();
                String  jsonBody =threadHandleJson.requestJsonFromAPI();
                threadHandleJson.callBack.getSongList(threadHandleJson.getListOfSongInRecommend(jsonBody));
                for (Song song : list){
                    if (threadHandleJson.isCancelled() ) {
                        threadHandleJson.setCurrentTask(ThreadHandleJson.STOP_THREAD);
                        break;
                    }
                    threadHandleJson.getBitmap(song);
                    Log.i("Song" , song.getLinkImage());
                }
            }

            @Override
            public void setupForAvailableList() throws Exception {
                threadHandleJson.setCurrentTask(ThreadHandleJson.AVAILABLE_LIST);
                Log.i("available ", "available");
                for(Song song :list ){
                    if (threadHandleJson.isCancelled() ) {
                        threadHandleJson.setCurrentTask(ThreadHandleJson.STOP_THREAD);
                        break;
                    }
                    threadHandleJson.getBitmap(song);
                    Log.i("Song" , song.getLinkImage());
                }

            }
        } ;

    }


    public void setCallbackOfManager ( @NonNull Callback callbackOfManager){
        this.callbackOfManager = callbackOfManager ;
     //   setCallbackDataInThread(callbackOfManager);

    }
    private void createAsyncTask (){
        threadHandleJson = new ThreadHandleJson() ;
        setCallbackDataInThread(callbackOfManager);
        setSetupDataInThread () ;

    }
    public void execute ( String search ) {
        delayThread.search = search ;
        delayThread.list=null;
        if (!delayThread.isAlive()&& !delayThread.ac){
            delayThread.start();
        }else if (!delayThread.isAlive()&& delayThread.ac){
            delayThread = new DelayThread() ;
            delayThread.search = search ;
            delayThread.start();
        }
    }
    public void executeWithList ( List<Song> list ){

        delayThread.search = null ;
        delayThread.list = list ;
        if (!delayThread.isAlive() && !delayThread.ac){
            delayThread.start();
        }else if (!delayThread.isAlive() && delayThread.ac){
            delayThread = new DelayThread();
            delayThread.list = list;
            delayThread.start();

        }


    }


    private void setExecute (String search )  {
        if (threadHandleJson.getCurrentTask() != threadHandleJson.STOP_THREAD) threadHandleJson.cancel(true);
        while (threadHandleJson.getCurrentTask() != ThreadHandleJson.STOP_THREAD)
        {}; // waitStopThread
        createAsyncTask();

        try {
            Thread.sleep(1); // wait for thread Surely stopp
            if (search == null) {
                threadHandleJson.setCurrentTask(ThreadHandleJson.LIST_RECOMMENDATION);
            }else {
                keyWord = search ;
                threadHandleJson.setCurrentTask(ThreadHandleJson.SEARCH);
            }
            threadHandleJson.execute() ;
        }catch (Exception e) {
            Log.i ("wait Thread Exception" , e.getMessage()) ;
        }
    }

    private void setExecuteWithList (List<Song> list ){
        if (threadHandleJson.getCurrentTask() != threadHandleJson.STOP_THREAD) threadHandleJson.cancel(true);
        while (threadHandleJson.getCurrentTask() != ThreadHandleJson.STOP_THREAD)
        {}; // waitStopThread
        createAsyncTask();
        try {
            Thread.sleep(1); // wait for thread Surely stopp
            if (list != null) {
                threadHandleJson.setCurrentTask(ThreadHandleJson.AVAILABLE_LIST);

            }
            threadHandleJson.execute() ;
        }catch (Exception e) {
            Log.i ("wait Thread Exception" , e.getMessage()) ;
        }
    }




    public interface Callback {
        void loadUI (Song song) ; // load UI in mainthread
    }
    class DelayThread extends Thread {
        public String search = null;
        public boolean ac = false ;
        public List<Song> list  = null;

        @Override
        public void run() {
            if (list == null) {
                setExecute(search);
                ac = true;
            }
            else {
                setExecuteWithList(list);
                ac = true;
            }
        }
    }


    
}
