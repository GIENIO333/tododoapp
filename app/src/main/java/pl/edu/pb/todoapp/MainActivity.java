package pl.edu.pb.todoapp;

import android.content.Context;
import android.content.Intent;
//Hostuje fragment z edycją zadania
import androidx.fragment.app.Fragment;

import java.util.UUID;

public class MainActivity extends SingleFragmentActivity {
    public static final String KEY_EXTRA_TASK_ID = "task_id";

    public static Intent newIntent(Context packageContext, UUID taskId) {
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(KEY_EXTRA_TASK_ID, taskId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID taskId = (UUID) getIntent().getSerializableExtra(KEY_EXTRA_TASK_ID);
        return TaskFragment.newInstance(taskId);
    }
}
