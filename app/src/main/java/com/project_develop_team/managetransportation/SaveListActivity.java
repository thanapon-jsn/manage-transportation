package com.project_develop_team.managetransportation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project_develop_team.managetransportation.models.Tasks;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SaveListActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    public static final String EXTRA_TASKS_KEY = "tasks-key";

    @BindView(R.id.nameTextView)
    TextView nameTextView;
    @BindView(R.id.task_name_text_view)
    TextView taskNameTextView;
    @BindView(R.id.task_address_text_view)
    TextView taskAddressTextView;
    @BindView(R.id.task_phone_text_view)
    TextView taskPhoneTextView;

    private ValueEventListener mEventListener;

    String tasksKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_list);
        ButterKnife.bind(this);

        tasksKey = getIntent().getStringExtra(EXTRA_TASKS_KEY);
        if (tasksKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_TASKS_KEY");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("tasks").child(tasksKey);

    }

    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tasks tasks = dataSnapshot.getValue(Tasks.class);
                nameTextView.setText(tasks.name);
                taskNameTextView.setText(tasks.task_name);
                taskAddressTextView.setText(tasks.task_address);
                taskPhoneTextView.setText("โทร" + " " + tasks.task_phone);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Fail load task", Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addValueEventListener(eventListener);

        mEventListener = eventListener;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mEventListener != null) {
            databaseReference.removeEventListener(mEventListener);
        }
    }
}
