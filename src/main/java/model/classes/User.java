package model.classes;

import model.interfaces.IUser;

public class User implements IUser {

    private String name;

    private String username;

    public User(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
