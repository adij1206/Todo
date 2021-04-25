package com.example.todo.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.todo.Model.Medicine;

import com.example.todo.Util.Constants;
import com.example.todo.Util.ReminderConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReminderDatabaseHelper extends SQLiteOpenHelper {
    Context ctx;

    public ReminderDatabaseHelper(@Nullable  Context context){
        super(context, ReminderConstants.DB_NAME, null, ReminderConstants.DB_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE "+ ReminderConstants.TABLE_NAME + "("
                + ReminderConstants.KEY_ID + " INTEGER PRIMARY KEY,"
                + ReminderConstants.KEY_REMINDER_NAME + " TEXT,"
                + ReminderConstants.KEY_REMINDER_HR +" INTEGER,"
                + ReminderConstants.KEY_REMINDER_MIN + " INTEGER,"
                + ReminderConstants.KEY_REMINDER_SET + " INTEGER,"
                +ReminderConstants.KEY_REMINDER_DATE_NAME + " LONG);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + ReminderConstants.TABLE_NAME);
        onCreate(db);
    }

    public void addMedicine(Medicine medicine){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ReminderConstants.KEY_REMINDER_NAME,medicine.getName());
        values.put(ReminderConstants.KEY_REMINDER_HR,medicine.getHr());
        values.put(ReminderConstants.KEY_REMINDER_MIN,medicine.getMin());
        values.put(ReminderConstants.KEY_REMINDER_SET,medicine.getSet());
        values.put(ReminderConstants.KEY_REMINDER_DATE_NAME,java.lang.System.currentTimeMillis());

        db.insert(ReminderConstants.TABLE_NAME,null,values);
        Log.d("Saved", "Saving in db ");
    }

    //Get a Medicine
    public Medicine getMedicine(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ReminderConstants.TABLE_NAME,new String[] {ReminderConstants.KEY_ID,ReminderConstants.KEY_REMINDER_NAME,
                        ReminderConstants.KEY_REMINDER_HR,ReminderConstants.KEY_REMINDER_MIN,ReminderConstants.KEY_REMINDER_SET,ReminderConstants.KEY_REMINDER_DATE_NAME},Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)},null,null,null,null);

        if(cursor !=null)
            cursor.moveToFirst();

        Medicine medicine = new Medicine();
        medicine.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ReminderConstants.KEY_ID))));
        medicine.setName(cursor.getString(cursor.getColumnIndex(ReminderConstants.KEY_REMINDER_NAME)));
        medicine.setHr(cursor.getInt(cursor.getColumnIndex(ReminderConstants.KEY_REMINDER_HR)));
        medicine.setMin(cursor.getInt(cursor.getColumnIndex(ReminderConstants.KEY_REMINDER_MIN)));
        medicine.setSet(cursor.getInt(cursor.getColumnIndex(ReminderConstants.KEY_REMINDER_SET)));

        java.text.DateFormat dateFormat  = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(ReminderConstants.KEY_REMINDER_DATE_NAME))).getTime());

        medicine.setDateAdded(formattedDate);
        return medicine;
    }

    //Get all Medicine
    public List<Medicine> getAllMedicine(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Medicine> medicineList = new ArrayList<>();

        Cursor cursor = db.query(ReminderConstants.TABLE_NAME,new String[] {ReminderConstants.KEY_ID,ReminderConstants.KEY_REMINDER_NAME,
                        ReminderConstants.KEY_REMINDER_HR,ReminderConstants.KEY_REMINDER_MIN,ReminderConstants.KEY_REMINDER_SET,ReminderConstants.KEY_REMINDER_DATE_NAME},null,
                null,null,null,ReminderConstants.KEY_REMINDER_DATE_NAME + " DESC");

        if(cursor.moveToFirst())
        {
            do{
                Medicine medicine = new Medicine();
                medicine.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ReminderConstants.KEY_ID))));
                medicine.setName(cursor.getString(cursor.getColumnIndex(ReminderConstants.KEY_REMINDER_NAME)));
                medicine.setHr(cursor.getInt(cursor.getColumnIndex(ReminderConstants.KEY_REMINDER_HR)));
                medicine.setMin(cursor.getInt(cursor.getColumnIndex(ReminderConstants.KEY_REMINDER_MIN)));
                medicine.setSet(cursor.getInt(cursor.getColumnIndex(ReminderConstants.KEY_REMINDER_SET)));

                java.text.DateFormat dateFormat  = java.text.DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(ReminderConstants.KEY_REMINDER_DATE_NAME))).getTime());

                medicine.setDateAdded(formattedDate);

                medicineList.add(medicine);

            }while (cursor.moveToNext());
        }
        return medicineList;
    }

    //Update Medicine
    public int updateMedicine(Medicine medicine){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ReminderConstants.KEY_REMINDER_NAME,medicine.getName());
        values.put(ReminderConstants.KEY_REMINDER_HR,medicine.getHr());
        values.put(ReminderConstants.KEY_REMINDER_MIN,medicine.getMin());
        values.put(ReminderConstants.KEY_REMINDER_SET,medicine.getSet());
        values.put(ReminderConstants.KEY_REMINDER_DATE_NAME,java.lang.System.currentTimeMillis());

        return db.update(ReminderConstants.TABLE_NAME,values,ReminderConstants.KEY_ID + "=?",new String[]{String.valueOf(medicine.getId())});

    }

    //Delete Medicine
    public void deleteMedicine(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ReminderConstants.TABLE_NAME,ReminderConstants.KEY_ID + "=?",new String[]{String.valueOf(id)});
        db.close();
    }

    // Get count of all Medicine
    public int getMedicineCount(){
        String countQuery = "SELECT * FROM " + ReminderConstants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery,null);

        return cursor.getCount();
    }

}
