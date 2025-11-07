package pl.edu.pb.todoapp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//Singleton przechowujący wszystkie zadania
public class TaskStorage {
    private static final TaskStorage taskStorage = new TaskStorage();
    private final List<Task> tasks;

    public static TaskStorage getInstance() {
        return taskStorage;
    }

    private TaskStorage() {
        tasks = new ArrayList<>();
        Task.Category[] categories = Task.Category.values();
        for (int i = 1; i <= 150; i++) {
            Task task = new Task();
            task.setName("Pilne zadanie numer " + i);
            task.setDone(i % 3 == 0);
            task.setCategory(categories[i % categories.length]);
            tasks.add(task);
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Task> getTasks(Task.Category category) {
        if (category == null) {
            return getTasks();
        }
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getCategory() == category) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }

    public Task getTask(UUID id) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                return task;
            }
        }
        return null;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public int getTaskCount(Task.Category category) {
        if (category == null) {
            return tasks.size();
        }
        int count = 0;
        for (Task task : tasks) {
            if (task.getCategory() == category) {
                count++;
            }
        }
        return count;
    }
}
