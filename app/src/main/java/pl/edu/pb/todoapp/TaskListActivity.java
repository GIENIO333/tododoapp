package pl.edu.pb.todoapp;
//Hostuje fragment z listą zadań
import android.os.Bundle;
import androidx.fragment.app.Fragment;

public class TaskListActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    protected Fragment createFragment() {
        return new TaskListFragment();
    }
}