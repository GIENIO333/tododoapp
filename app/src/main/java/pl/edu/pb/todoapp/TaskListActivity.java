package pl.edu.pb.todoapp;
//Hostuje fragment z listą zadań
import androidx.fragment.app.Fragment;

public class TaskListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new TaskListFragment();
    }
}