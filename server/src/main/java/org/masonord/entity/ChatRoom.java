package org.masonord.entity;

import java.util.ArrayList;

public class ChatRoom {

    private String name;
    private ArrayList<Integer> usersId;

    public ArrayList<Integer> getUsers() {
        return usersId;
    }

    public void setUsers(ArrayList<Integer> usersId) {
        this.usersId = usersId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
