package com.example.todo.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.todo.Model.Note;
import com.example.todo.Util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private Context ctx;
    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.ctx  = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE "+ Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_NOTE_TITLE + " TEXT,"
                + Constants.KEY_NOTE_DESP +" TEXT,"
                +Constants.KEY_DATE_NAME + " LONG);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    public void addNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_NOTE_TITLE,note.getTitle());
        values.put(Constants.KEY_NOTE_DESP,note.getDescription());
        values.put(Constants.KEY_DATE_NAME,java.lang.System.currentTimeMillis());

        db.insert(Constants.TABLE_NAME,null,values);
        Log.d("Saved", "Saving in db ");
    }

    //Get a grocery
    public Note getNote(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME,new String[] {Constants.KEY_ID,Constants.KEY_NOTE_TITLE,
                        Constants.KEY_NOTE_DESP,Constants.KEY_DATE_NAME},Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)},null,null,null,null);

        if(cursor !=null)
            cursor.moveToFirst();

        Note note = new Note();
        note.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
        note.setTitle(cursor.getString(cursor.getColumnIndex(Constants.KEY_NOTE_TITLE)));
        note.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_NOTE_DESP)));

        java.text.DateFormat dateFormat  = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

        note.setDateAdded(formattedDate);

        return note;
    }

    //Get all Grocery
    public List<Note> getAllNote(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> noteList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME,new String[] {Constants.KEY_ID,Constants.KEY_NOTE_TITLE,
                        Constants.KEY_NOTE_DESP,Constants.KEY_DATE_NAME},null,
                null,null,null,Constants.KEY_DATE_NAME + " DESC");

        if(cursor.moveToFirst())
        {
            do{
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                note.setTitle(cursor.getString(cursor.getColumnIndex(Constants.KEY_NOTE_TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_NOTE_DESP)));

                java.text.DateFormat dateFormat  = java.text.DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

                note.setDateAdded(formattedDate);

                noteList.add(note);

            }while (cursor.moveToNext());
        }
        return noteList;
    }

    //Update Grocery
    public int updateNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_NOTE_TITLE,note.getTitle());
        values.put(Constants.KEY_NOTE_DESP,note.getDescription());
        values.put(Constants.KEY_DATE_NAME,java.lang.System.currentTimeMillis());

        return db.update(Constants.TABLE_NAME,values,Constants.KEY_ID + "=?",new String[]{String.valueOf(note.getId())});

    }

    //Delete Grocery
    public void deleteNote(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME,Constants.KEY_ID + "=?",new String[]{String.valueOf(id)});
        db.close();
    }

    // Get count of all groceries
    public int getNoteCount(){
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery,null);

        return cursor.getCount();
    }

}
