package com.guoxiaoxing.kitty.model;


/**
 * @author guoxiaoxing
 */
public class Todo {

    public long id;
    public String message;
    public boolean completed;

    public Todo(long id, String message, boolean completed) {
        this.id = id;
        this.message = message;
        this.completed = completed;
    }

    public Todo clone() {
        return new Todo(this.id, this.message, this.completed);
    }
}
