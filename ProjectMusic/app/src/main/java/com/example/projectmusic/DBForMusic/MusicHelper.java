package com.example.projectmusic.DBForMusic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.zip.Inflater;

 class MusicHelper extends SQLiteOpenHelper {

    public final int VERSION = 1 ;
    public MusicHelper(@Nullable Context context) {
        super(context, InforAboutTable.DATABASE_NAME, null, 1 );

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE  " +
                InforAboutTable.TABLE1_NAME +"("
        + InforAboutTable.IMAGE_COLUMN_OF_TABLE1 +" TEXT ,"
        + InforAboutTable.NAME_COLUMN_OF_TABLE1 + " TEXT ,"
        + InforAboutTable.SONG_COLUMN_OF_TABLE1 + " TEXT PRIMARY KEY,"
        + InforAboutTable.AUTHOR_COLUMN_OF_TABLE1 + " TEXT )");
        Log.i("creat db " ,"CREATE TABLE  " +
                InforAboutTable.TABLE1_NAME +"("
                + InforAboutTable.IMAGE_COLUMN_OF_TABLE1 +" TEXT ,"
                + InforAboutTable.NAME_COLUMN_OF_TABLE1 + " TEXT ,"
                + InforAboutTable.SONG_COLUMN_OF_TABLE1 + " TEXT PRIMARY KEY,"
                + InforAboutTable.AUTHOR_COLUMN_OF_TABLE1 + " TEXT )" ) ;

    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }

}
