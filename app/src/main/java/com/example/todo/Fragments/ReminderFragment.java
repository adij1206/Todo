package com.example.todo.Fragments;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todo.Activities.MainActivity;
import com.example.todo.Data.DatabaseHandler;
import com.example.todo.Data.ReminderDatabaseHelper;
import com.example.todo.Model.Medicine;
import com.example.todo.Model.Note;
import com.example.todo.Notifications.AlarmReceiver;
import com.example.todo.R;
import com.example.todo.UI.RecylcerViewAdapter;
import com.example.todo.UI.ReminderAdapter;
import com.example.todo.Util.ReminderConstants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderFragment extends Fragment {

    private View reminderView;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private int setRem = 0;

    TimePickerDialog timePickerDialog;
    Calendar calSet;

    EditText addListName;
    TextView addListTime;
    private Button saveBtn,timePickerBtn;

    private ReminderDatabaseHelper db;
    private RecyclerView recyclerView;
    private List<Medicine> medicineList;
    private List<Medicine> listItem;
    private ReminderAdapter recylcerViewAdapter;

    public ReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        reminderView =  inflater.inflate(R.layout.fragment_reminder, container, false);

        FloatingActionButton floatingActionButton = reminderView.findViewById(R.id.fab_add_reminder_btn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createReminderPopup();
            }
        });

        db = new ReminderDatabaseHelper(reminderView.getContext());
        recyclerView = (RecyclerView) reminderView.findViewById(R.id.reminder_recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(reminderView.getContext()));

        medicineList = new ArrayList<>();
        listItem = new ArrayList<>();

        //db.deleteMedicine(1);
        medicineList = db.getAllMedicine();

        for(Medicine m: medicineList){
            Medicine medicine = new Medicine();
            medicine.setName(m.getName());
            medicine.setHr(m.getHr());
            medicine.setId(m.getId());
            medicine.setMin(m.getMin());
            medicine.setSet(m.getSet());
            medicine.setDateAdded(m.getDateAdded());

            listItem.add(medicine);
        }

        recylcerViewAdapter = new ReminderAdapter(reminderView.getContext(),listItem);
        recyclerView.setAdapter(recylcerViewAdapter);
        recylcerViewAdapter.notifyDataSetChanged();

        return reminderView;
    }


    private void saveToDb(View v) {
        Medicine medicine = new Medicine();
        String title = addListName.getText().toString();

        String am_pm="", hour="", min="";
        if (calSet.get(Calendar.AM_PM)==1){
            am_pm = "PM";
        }else{
            am_pm = "AM";
        }

        if (calSet.get(Calendar.HOUR)==0){
            hour="12";
        }else{
            hour= String.valueOf(calSet.get(Calendar.HOUR));
        }
        if (calSet.get(Calendar.MINUTE)==0){
            min="00";
        }else{
            min= String.valueOf(calSet.get(Calendar.MINUTE));
        }

        medicine.setName(title);
        medicine.setHr(Integer.parseInt(hour));
        medicine.setMin(Integer.parseInt(min));
        medicine.setSet(setRem);

        db.addMedicine(medicine);

        Snackbar.make(v,"Item Saved",Snackbar.LENGTH_SHORT).show();
        Log.d("Saved Id :",String.valueOf(db.getMedicineCount()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(reminderView.getContext(), MainActivity.class));
            }
        },1200);

    }

    private void createReminderPopup(){
        builder = new AlertDialog.Builder(reminderView.getContext());
        View view = getLayoutInflater().inflate(R.layout.popup_reminder,null);

        addListName = (EditText) view.findViewById(R.id.popup_reminder_item_name);
        addListTime = (TextView) view.findViewById(R.id.popup_reminder_item_desp);
        saveBtn = (Button) view.findViewById(R.id.popup_reminder_save_btn);
        timePickerBtn = (Button) view.findViewById(R.id.popup_reminder_open_timepicker);

        builder.setView(view);

        dialog = builder.create();
        dialog.show();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDb(v);
                //Toast.makeText(MainActivity.this,"save Pressed",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        timePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePickerDialog(false);
            }
        });

    }

    private void openTimePickerDialog(boolean is24r){
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(
                getActivity(),
                onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                is24r);
        timePickerDialog.setTitle("Set Reminder Time");
        timePickerDialog.show();
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar calNow = Calendar.getInstance();
            calSet = (Calendar) calNow.clone();
            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            setRem = 1;
            calSet.set(Calendar.MILLISECOND, 0);
            if(calSet.compareTo(calNow) <= 0){
                calSet.add(Calendar.DATE, 1);
            }
            setAlarm(calSet);

        }};


    private void setAlarm(Calendar targetCal){

        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        String am_pm="", hour="", min="";
        if (targetCal.get(Calendar.AM_PM)==1){
            am_pm = "PM";
        }else{
            am_pm = "AM";
        }

        if (targetCal.get(Calendar.HOUR)==0){
            hour="12";
        }else{
            hour= String.valueOf(targetCal.get(Calendar.HOUR));
        }
        if (targetCal.get(Calendar.MINUTE)==0){
            min="00";
        }else{
            min= String.valueOf(targetCal.get(Calendar.MINUTE));
        }

        addListTime.setText(hour+":"+min+" "+am_pm);

    }

}
