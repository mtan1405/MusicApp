package com.example.projectmusic;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class DetailActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detai_act);
        addFragment() ;
    }
    public void addFragment(){
        Fragment fragment = ControlFragment.newInstance();
        getSupportFragmentManager().
                beginTransaction().
                add(R.id.fragment_container , fragment ).commit();

    }
}
