package com.example.projectmusic.DBForMusic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.projectmusic.Application2;
import com.example.projectmusic.PlayListManager.PlayListMusic.Song;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private MusicHelper musicHelper ;
    private SQLiteDatabase database ;
    private static DBManager dbManager ;
    private DBManager (Context context)
    {

        musicHelper = new MusicHelper(context) ;
        database = musicHelper.getWritableDatabase();

    }
    public  static DBManager getInstance(){
        if (dbManager == null) {
            dbManager = new DBManager(Application2.getContext()) ;

        }return dbManager ;
    }
    public  void insert (Song song ){


            String query = "INSERT INTO " + InforAboutTable.TABLE1_NAME + "("
                    + InforAboutTable.NAME_COLUMN_OF_TABLE1 + ","
                    + InforAboutTable.SONG_COLUMN_OF_TABLE1 + ","
                    + InforAboutTable.IMAGE_COLUMN_OF_TABLE1 + ","
                    + InforAboutTable.AUTHOR_COLUMN_OF_TABLE1
                    + ") " + "VALUES ( '" + song.getName() + "'"
                    + ", '" + song.getLinkSong() + "' , '" + song.getLinkImage() + "' , '" + song.getAuthor() + "')";
            Log.i("insert query ", query);
            database.execSQL(query);
            Toast.makeText(Application2.getContext(), "Đã thêm "+song.getName(), Toast.LENGTH_SHORT).show();


    }
    public void delete (Song song ){
        try {

            String query = "DELETE FROM " + InforAboutTable.TABLE1_NAME +
                    " WHERE " + InforAboutTable.SONG_COLUMN_OF_TABLE1 +
                    " = '" + song.getLinkSong() + "';";
            database.execSQL(query);
            Toast.makeText(Application2.getContext(), "Đã Xóa  "+song.getName(), Toast.LENGTH_SHORT).show();

        }catch (Exception e) {
            Toast.makeText(Application2.getContext()," Lỗi khi xóa  "+ song.getName() +" rồi ", Toast.LENGTH_SHORT).show();



        }
    }
    public List<Song> getAllSong ()  {
        List<Song> list  = new ArrayList<>(10);
        Cursor cursor = database.rawQuery("SELECT * FROM "+ InforAboutTable.TABLE1_NAME , null ) ;

        cursor.moveToFirst();
        int indexSongName =  cursor.getColumnIndex(InforAboutTable.NAME_COLUMN_OF_TABLE1);
        int indexLinkOfSong = cursor.getColumnIndex(InforAboutTable.SONG_COLUMN_OF_TABLE1) ;
        int indexAuthor = cursor.getColumnIndex(InforAboutTable.AUTHOR_COLUMN_OF_TABLE1) ;
        int indexImageLink = cursor.getColumnIndex(InforAboutTable.IMAGE_COLUMN_OF_TABLE1) ;
        Log.i("count" , String.valueOf(cursor.getCount())) ;
        while (cursor.moveToNext()){

           list.add(new Song(cursor.getString(indexImageLink) ,cursor.getString(indexLinkOfSong) , cursor.getString(indexSongName),cursor.getString(indexAuthor))) ;

        }
        return list ;
    }





}
