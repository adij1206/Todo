package com.example.todo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.todo.R;

public class DetailActivity extends AppCompatActivity {

    private TextView detailTitle,detailDate,detailDesp;
    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailTitle = (TextView) findViewById(R.id.detail_note_title);
        detailDate = (TextView) findViewById(R.id.detail_note_date);
        detailDesp = (TextView) findViewById(R.id.detail_note_description);

        Bundle extras = getIntent().getExtras();

        if(extras!=null){
            detailTitle.setText(extras.getString("title"));
            detailDesp.setText(extras.getString("description"));
            detailDate.setText(extras.getString("date"));
            noteId = extras.getInt("id");
        }
    }
}
