package pl.edu.pb.todoapp;

import android.os.Bundle;
//Hostuje fragment z edycją zadania
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.UUID;

public class MainActivity extends SingleFragmentActivity {
    public static final String KEY_EXTRA_TASK_ID = "task_id";

    @Override
    protected Fragment createFragment() {
        UUID taskId = (UUID) getIntent().getSerializableExtra(KEY_EXTRA_TASK_ID);
        return TaskFragment.newInstance(taskId);

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}