package com.ferguson.clean;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TrashList extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_list);
        initComponents();


    }

    private void initComponents(){
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        rv = (RecyclerView) findViewById(R.id.recycler_view);
    }
}
