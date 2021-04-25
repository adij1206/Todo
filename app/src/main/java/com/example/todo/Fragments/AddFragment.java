package com.example.todo.Fragments;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.app.TimePickerDialog;

import com.example.todo.Activities.MainActivity;
import com.example.todo.Data.DatabaseHandler;
import com.example.todo.Model.Note;
import com.example.todo.Notifications.AlarmReceiver;
import com.example.todo.Notifications.MyNotificationPublisher;
import com.example.todo.R;
import com.example.todo.UI.RecylcerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {

    private Context ctx;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private EditText addListName,addListDesp;
    private Button saveBtn,openTime,cancelAlarm;

    private View noteView;
    private DatabaseHandler db;
    private RecyclerView recyclerView;
    private List<Note> noteList;
    private List<Note> listItem;
    private RecylcerViewAdapter recylcerViewAdapter;
    //final Calendar myCalender = Calendar.getInstance();

    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

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

        db = new DatabaseHandler(noteView.getContext());
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
        //openTime = (Button) view.findViewById(R.id.popup_add_noti_btn);
        //cancelAlarm = (Button) view.findViewById(R.id.popup_add_can_btn);


        builder.setView(view);
        //builder.setTitle("Add Notes");
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

        /*openTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*DialogFragment timePicker = new TimePickerFragment();
                Log.d("Adi", "onClick: Yes");
                timePicker.show(getActivity().getSupportFragmentManager(),"time_picker");

                new TimePickerDialog(
                        getContext(), onTimeSetListener,
                        myCalender.get(Calendar.HOUR_OF_DAY ) ,
                        myCalender.get(Calendar.MINUTE ) , DateFormat.is24HourFormat(getActivity())
                ).show() ;
            }
        });

        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelalaram();
            }
        });*/

    }

    /*private void startAlarm(Calendar calendar) {
        Log.d("Adi", "startAlarm: Strating");
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),1,intent,0);

        if(calendar.after(Calendar.getInstance())){
            calendar.add(Calendar.DATE,1);
        }



        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }*/
    /*private void updateLabel () {
        String myFormat = "dd/MM/yy" ; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat , Locale.getDefault ()) ;
        Date date = myCalender.getTime() ;
        //btnDate .setText(sdf.format(date)) ;
        scheduleNotification(getNotification(sdf.format(date) ) , date.getTime()) ;
    }

    private void scheduleNotification (Notification notification , long delay) {
        Log.d("Adi", "scheduleNotification: ok");
        Intent notificationIntent = new Intent( getContext(), MyNotificationPublisher.class ) ;
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( getContext(), 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , delay , pendingIntent) ;
    }
    private Notification getNotification (String content) {
        Log.d("Adi", "scheduleNotification: ok1");
        NotificationCompat.Builder builder = new NotificationCompat.Builder( getContext(), default_notification_channel_id ) ;
        builder.setContentTitle( "Scheduled Notification" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }

    private void cancelalaram() {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),1,intent,0);

        alarmManager.cancel(pendingIntent);
        Log.d("Adi", "cancelalaram: Done");
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalender.set(Calendar.HOUR_OF_DAY , hourOfDay) ;
            myCalender.set(Calendar.MINUTE , minute) ;
            myCalender.set(Calendar.SECOND , 0) ;
            Log.d("Adi", "scheduleNotification: ok2");
            updateLabel() ;

        }
    };

    /*@Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.d("Adi", "onTimeSet: Done");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);

        Log.d("Adi", "onTimeSet: Done");

        //startAlarm(calendar);
        updateLabel();
    }*/
}
