package com.example.projectmusic;

import android.app.Application;
import android.content.Context;

public class Application2 extends Application {
    public Application2 () {}
    private static Application2 application ;
    @Override
    public void onCreate() {
        super.onCreate();
       application = this;
    }

    public static Context getContext (){
        return application;
    }
}
