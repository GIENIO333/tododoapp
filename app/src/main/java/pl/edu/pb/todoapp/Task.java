package pl.edu.pb.todoapp;

import java.util.Date;
import java.util.UUID;

//Przechowuje dane pojedynczego zadania
public class Task {
    public enum Category {
        HOME,
        STUDIES,
        OTHER
    }

    private UUID id;
    private String name;
    private Date date;
    private boolean done;
    private Category category;

    public Task() {
        id = UUID.randomUUID();
        date = new Date();
        category = Category.OTHER;
    }

    // Gettery i settery
    public UUID getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
