package model.classes;

import model.interfaces.IUser;

public class User implements IUser {

    private String name;

    public User(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
