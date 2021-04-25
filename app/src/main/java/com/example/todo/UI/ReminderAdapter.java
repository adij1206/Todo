package com.example.todo.UI;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Data.ReminderDatabaseHelper;
import com.example.todo.Model.Medicine;
import com.example.todo.Model.Note;
import com.example.todo.Notifications.AlarmReceiver;
import com.example.todo.R;

import java.util.Calendar;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    Context context;
    List<Medicine> medicineList;

    public ReminderAdapter(Context context, List<Medicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_row_layout,null);
        return new ReminderAdapter.ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Medicine medicine = medicineList.get(position);

        holder.name.setText(medicine.getName());
        holder.time.setText(String.valueOf(medicine.getHr())+" : "+String.valueOf(medicine.getMin()));

        holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    medicine.setSet(0);
                    holder.db.updateMedicine(medicine);
                    Log.d("Adi", "onCheckedChanged: Changed to false");


                    Intent intent = new Intent(context, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1,intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                    am.cancel(pendingIntent);
                }
                else{
                   medicine.setSet(1);
                    holder.db.updateMedicine(medicine);

                    Calendar c = Calendar.getInstance();
                    Calendar calSet = (Calendar) c.clone();
                    calSet.set(Calendar.HOUR_OF_DAY, medicine.getHr());
                    calSet.set(Calendar.MINUTE, medicine.getMin());
                    calSet.set(Calendar.SECOND, 0);
                    calSet.set(Calendar.MILLISECOND, 0);
                    if(calSet.compareTo(c) <= 0){
                        calSet.add(Calendar.DATE, 1);
                    }
                    Log.i("ff",c.get(Calendar.HOUR_OF_DAY)+": "+c.get(Calendar.MINUTE)+" hh "+c.getTimeInMillis());

                    setLastAlarm(calSet);

                }
            }
        });

        if(medicine.getSet()==1){
            holder.switchCompat.setChecked(true);

            Calendar c = Calendar.getInstance();
            Calendar calSet = (Calendar) c.clone();
            calSet.set(Calendar.HOUR_OF_DAY, medicine.getHr());
            calSet.set(Calendar.MINUTE, medicine.getMin());
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            if(calSet.compareTo(c) <= 0){
                calSet.add(Calendar.DATE, 1);
            }
            Log.i("ff",c.get(Calendar.HOUR_OF_DAY)+": "+c.get(Calendar.MINUTE)+" hh "+c.getTimeInMillis());

            setLastAlarm(calSet);

        }
        else {
            holder.switchCompat.setChecked(false);
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1,intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            am.cancel(pendingIntent);
        }


    }
    private void setLastAlarm(Calendar targetCal){

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }


    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Context ctx;
        TextView name,time;
        Button editBtn,deleteBtn;
        SwitchCompat switchCompat;
        ReminderDatabaseHelper db;

        public ViewHolder(@NonNull View itemView,Context context) {
            super(itemView);
            ctx = context;

            db = new ReminderDatabaseHelper(ctx);

            name = itemView.findViewById(R.id.reminder_title);
            time = itemView.findViewById(R.id.reminder_timeAdd);

            switchCompat  = itemView.findViewById(R.id.set_reminder_switch);
            editBtn = itemView.findViewById(R.id.editButtonReminder);
            deleteBtn  = itemView.findViewById(R.id.deleteButtonReminder);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }


    }
}
