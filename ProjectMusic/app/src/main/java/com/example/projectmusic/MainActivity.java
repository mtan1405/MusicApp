package com.example.projectmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmusic.DBForMusic.DBManager;
import com.example.projectmusic.HandleJson.HandleManager;
import com.example.projectmusic.HandleJson.ThreadHandleJson;
import com.example.projectmusic.PlayListManager.PlayListManager;
import com.example.projectmusic.PlayListManager.PlayListMusic.Song;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    HandleManager  manager ;
    List<Song> songList ;
    AdapterForRecyclerView adapter ;
    HandleManager.Callback callbackManager ;
    AdapterForRecyclerView.DisableView disableView1 ;
    AdapterForRecyclerView.DisableView disableView2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act2 );
        songList = new ArrayList<>() ;
        PlayListManager.getPlayListManager().currentList = songList;
        adapter = new AdapterForRecyclerView(songList , this) ;
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this  , LinearLayoutManager.VERTICAL ,false));
        recyclerView.setAdapter(adapter);
        manager = HandleManager.getInstance(songList , callbackManager ,new HandleManager.Callback() {
            @Override
            public void loadUI(Song song) {

                adapter.notifyDataSetChanged();

            }
        }) ;

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.disableView= disableView1;
                EditText textView = findViewById(R.id.edtSearch) ;
                manager.execute(String.valueOf(textView.getText()));

            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.disableView= disableView1;
                manager.execute(null);
            }
        });
        adapter.setClickView(new AdapterForRecyclerView.ClickView() {
            @Override
            public void clickImageSong() {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class) ;
                MainActivity.this.startActivity(intent);
            }

            @Override
            public void addSong(Song song) {
                PlayListManager.getPlayListManager().addMyList(song);
            }

            @Override
            public void removeSong(Song song) {
                try {
                    PlayListManager.getPlayListManager().removeMyList(song);
                    songList.remove(song);
                    adapter.notifyDataSetChanged();
                }catch (Exception e ){
                    Toast.makeText(MainActivity.this, "Lỗi ", Toast.LENGTH_SHORT).show();


                }
            }
        });
        findViewById(R.id.get_list_).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.disableView= disableView2;
                PlayListManager.getPlayListManager().chooseMyList();
                List<Song> l = PlayListManager.getPlayListManager().currentList;
                manager.executeWithList(l);
           }
        });
        addFragment();
        startService(new Intent(this, MusicService.class));
        setUpDisable();

    }

    void setUpDisable (){
        disableView1 = new AdapterForRecyclerView.DisableView() {
            @Override
            public void disable(View view) {
                View view1 =    view.findViewById(R.id.add_song);
                view1 .setVisibility(View.VISIBLE);
                View view2 = view.findViewById(R.id.remove);
                view2.setVisibility(View.GONE);

            }
        };
        disableView2 =new AdapterForRecyclerView.DisableView() {
            @Override
            public void disable(View view) {

               View view1 = view.findViewById(R.id.remove) ;
                view1.setVisibility(View.VISIBLE);
               View view2 = view.findViewById(R.id.add_song);
                view2.setVisibility(View.GONE);
            }
        } ;
    }
    public void addFragment(){
        Fragment fragment = ControlFragment.newInstance();
        getSupportFragmentManager().
                beginTransaction().
                add(R.id.frameLayout , fragment ).commit();

    }


}
