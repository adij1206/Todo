package com.example.todo.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todo.Activities.MainActivity;
import com.example.todo.Data.DatabaseHandler;
import com.example.todo.Model.Note;
import com.example.todo.R;
import com.example.todo.UI.RecylcerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {

    private Context ctx;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private EditText addListName,addListDesp;
    private Button saveBtn;

    private View noteView;
    private DatabaseHandler db;
    private RecyclerView recyclerView;
    private List<Note> noteList;
    private List<Note> listItem;
    private RecylcerViewAdapter recylcerViewAdapter;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        noteView = inflater.inflate(R.layout.fragment_add, container, false);
        
        FloatingActionButton floatingActionButton = noteView.findViewById(R.id.fab_add_btn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopup();
            }
        });

        db= new DatabaseHandler(noteView.getContext());
        recyclerView = (RecyclerView) noteView.findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(noteView.getContext()));

        noteList = new ArrayList<>();
        listItem = new ArrayList<>();

        noteList = db.getAllNote();

        for(Note n : noteList){
            Note note = new Note();
            note.setTitle(n.getTitle());
            note.setDescription( n.getDescription());
            note.setId(n.getId());
            note.setDateAdded( n.getDateAdded());

            listItem.add(note);
        }

        recylcerViewAdapter = new RecylcerViewAdapter(noteView.getContext(),listItem);
        recyclerView.setAdapter(recylcerViewAdapter);
        recylcerViewAdapter.notifyDataSetChanged();
        return noteView;
    }

    private void saveToDb(View v) {
        Note note = new Note();

        String title = addListName.getText().toString();
        String desp = addListDesp.getText().toString();

        note.setTitle(title);
        note.setDescription(desp);

        db.addNote(note);

        Snackbar.make(v,"Item Saved",Snackbar.LENGTH_SHORT).show();

        Log.d("Saved Id :",String.valueOf(db.getNoteCount()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //STart a new Activity
                startActivity(new Intent(noteView.getContext(),MainActivity.class));
            }
        },1200);

    }

    private void createPopup() {
        builder = new AlertDialog.Builder(noteView.getContext());
        View view = getLayoutInflater().inflate(R.layout.popup_add,null);

        addListName = (EditText) view.findViewById(R.id.popup_add_item_name);
        addListDesp = (EditText) view.findViewById(R.id.popup_add_item_desp);
        saveBtn = (Button) view.findViewById(R.id.popup_add_save_btn);

        builder.setView(view);

        dialog = builder.create();
        dialog.show();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"save Pressed",Toast.LENGTH_SHORT).show();
                saveToDb(v);
                dialog.dismiss();
            }
        });
    }


}
