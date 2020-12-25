package com.example.todo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todo.Fragments.AddFragment;
import com.example.todo.Fragments.ListFragment;
import com.example.todo.Fragments.ReminderFragment;
import com.example.todo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private FloatingActionButton floatingActionButton;

    //private EditText addListName,addListDesp;
    //private Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new AddFragment());

        BottomNavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setOnNavigationItemSelectedListener(this);

       // floatingActionButton = findViewById(R.id.fab_add);


    }



    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragement_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.add_todo:
                fragment = new AddFragment();
               floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,"Fab Pressed",Toast.LENGTH_SHORT).show();
                       createPopup();
                    }
                });
                break;
            case R.id.add_list:
                fragment = new ListFragment();
                break;
            case R.id.add_reminder:
                fragment = new ReminderFragment();
                break;
        }

        return loadFragment(fragment);
    }

    private void createPopup() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_add,null);

        EditText addListName = (EditText) view.findViewById(R.id.popup_add_item_name);
        EditText addListDesp = (EditText) view.findViewById(R.id.popup_add_item_desp);
        Button saveBtn = (Button) view.findViewById(R.id.popup_add_save_btn);

        builder.setView(view);

        dialog = builder.create();
        dialog.show();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"save Pressed",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
