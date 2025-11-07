package pl.edu.pb.todoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

//Zarządza wyświetlaniem listy zadań
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.List;

public class TaskListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private static final String SAVED_CATEGORY = "category";

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private boolean subtitleVisible;
    private Task.Category currentCategory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            subtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE, false);
            if (savedInstanceState.containsKey(SAVED_CATEGORY)) {
                String categoryName = savedInstanceState.getString(SAVED_CATEGORY);
                if (categoryName != null) {
                    currentCategory = Task.Category.valueOf(categoryName);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, subtitleVisible);
        if (currentCategory != null) {
            outState.putString(SAVED_CATEGORY, currentCategory.name());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        recyclerView = view.findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (subtitleItem != null) {
            subtitleItem.setTitle(subtitleVisible ? R.string.hide_subtitle : R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_task:
                Task task = new Task();
                TaskStorage.getInstance().addTask(task);
                startActivity(MainActivity.newIntent(requireContext(), task.getId()));
                return true;
            case R.id.category_all:
                currentCategory = null;
                updateUI();
                return true;
            case R.id.category_home:
                currentCategory = Task.Category.HOME;
                updateUI();
                return true;
            case R.id.category_studies:
                currentCategory = Task.Category.STUDIES;
                updateUI();
                return true;
            case R.id.category_other:
                currentCategory = Task.Category.OTHER;
                updateUI();
                return true;
            case R.id.show_subtitle:
                subtitleVisible = !subtitleVisible;
                requireActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = currentCategory == null
                ? taskStorage.getTasks()
                : taskStorage.getTasks(currentCategory);

        if (adapter == null) {
            adapter = new TaskAdapter(tasks);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setTasks(tasks);
            adapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    private void updateSubtitle() {
        TaskStorage taskStorage = TaskStorage.getInstance();
        int taskCount = taskStorage.getTaskCount(currentCategory);
        String subtitle = getString(R.string.subtitle_format, taskCount);
        if (!subtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setSubtitle(subtitle);
        }
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView nameTextView;
        private final TextView dateTextView;
        private final ImageView iconImageView;
        private final CheckBox doneCheckBox;
        private final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        private Task task;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(this);

            nameTextView = itemView.findViewById(R.id.task_item_name);
            dateTextView = itemView.findViewById(R.id.task_item_date);
            iconImageView = itemView.findViewById(R.id.task_item_icon);
            doneCheckBox = itemView.findViewById(R.id.task_item_done);
        }

        public void bind(Task task) {
            this.task = task;
            nameTextView.setText(task.getName() != null ? task.getName() : "");
            dateTextView.setText(dateFormat.format(task.getDate()));
            iconImageView.setImageResource(getIconForCategory(task.getCategory()));

            doneCheckBox.setOnCheckedChangeListener(null);
            doneCheckBox.setChecked(task.isDone());
            doneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    task.setDone(isChecked);
                }
            });
        }

        @Override
        public void onClick(View v) {
            startActivity(MainActivity.newIntent(requireContext(), task.getId()));
        }

        private int getIconForCategory(Task.Category category) {
            if (category == null) {
                return R.drawable.ic_category_other;
            }
            switch (category) {
                case HOME:
                    return R.drawable.ic_category_home;
                case STUDIES:
                    return R.drawable.ic_category_studies;
                case OTHER:
                default:
                    return R.drawable.ic_category_other;
            }
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<Task> tasks;

        public TaskAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }

        public void setTasks(List<Task> tasks) {
            this.tasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = tasks.get(position);
            holder.bind(task);
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }
    }
}
