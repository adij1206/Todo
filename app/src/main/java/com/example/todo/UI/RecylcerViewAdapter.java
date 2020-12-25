package com.example.todo.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Activities.DetailActivity;
import com.example.todo.Activities.MainActivity;
import com.example.todo.Data.DatabaseHandler;
import com.example.todo.Model.Note;
import com.example.todo.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RecylcerViewAdapter extends RecyclerView.Adapter<RecylcerViewAdapter.ViewHolder> {

    private Context context;
    private List<Note> noteList;
    private LayoutInflater layoutInflater;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    public RecylcerViewAdapter(Context context,List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_note_row,null);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = noteList.get(position);

        holder.noteTitle.setText(note.getTitle());
        holder.noteDate.setText(note.getDateAdded());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView noteTitle,noteDate;
     public Button editBtn,deleteBtn;

        public ViewHolder(@NonNull View itemView,final Context ctx) {
            super(itemView);
            context = ctx;

            noteTitle = (TextView) itemView.findViewById(R.id.list_note_title);
            noteDate = (TextView) itemView.findViewById(R.id.list_note_dateAdd);
            editBtn = (Button) itemView.findViewById(R.id.editButton);
            deleteBtn = (Button) itemView.findViewById(R.id.deleteButton);

            editBtn.setOnClickListener(this);
            deleteBtn.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to next scrren/Detail Activity
                    int position = getAdapterPosition();

                    Note note = noteList.get(position);
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("title",note.getTitle());
                    intent.putExtra("description",note.getDescription());
                    intent.putExtra("id",note.getId());
                    intent.putExtra("date",note.getDateAdded());
                    context.startActivity(intent);

                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.editButton:
                    int position = getAdapterPosition();
                    Note note = noteList.get(position);
                    editItem(note);
                    break;

                case R.id.deleteButton :
                    int pos = getAdapterPosition();
                    Note note1= noteList.get(pos);
                    deleteItem(note1.getId());

                    break;
            }
        }

        public void deleteItem(final int id)
        {
            builder = new AlertDialog.Builder(context);
            layoutInflater = LayoutInflater.from(context);

            View view = layoutInflater.inflate(R.layout.confirmation_dialog,null);
            Button noButton = (Button) view.findViewById(R.id.noButton);
            Button yesButton = (Button) view.findViewById(R.id.yesButton);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteNote(id);
                    noteList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();
                    if(db.getNoteCount()==0){
                        context.startActivity(new Intent(context, MainActivity.class));
                    }
                }
            });

        }

        public void editItem(final Note note)
        {
            builder = new AlertDialog.Builder(context);
            layoutInflater = LayoutInflater.from(context);

            final View view = layoutInflater.inflate(R.layout.popup_add,null);

            final EditText popupTitle = (EditText) view.findViewById(R.id.popup_add_item_name);
            final EditText popupDesp = (EditText) view.findViewById(R.id.popup_add_item_desp);
            TextView title = (TextView) view.findViewById(R.id.popup_add_title);
            title.setText("Edit Note");
            Button saveButton = (Button) view.findViewById(R.id.popup_add_save_btn);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);

                    note.setTitle(popupTitle.getText().toString());
                    note.setDescription(popupDesp.getText().toString());

                    if(!popupTitle.getText().toString().isEmpty()
                            && !popupDesp.getText().toString().isEmpty()){
                        db.updateNote(note);

                        notifyItemChanged(getAdapterPosition(),note);
                    }else {
                        Snackbar.make(view,"Enter Note and Description",Snackbar.LENGTH_LONG);
                    }
                    dialog.dismiss();
                }
            });
        }


    }
}
