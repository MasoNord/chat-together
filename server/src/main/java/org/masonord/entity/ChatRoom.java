package org.masonord.entity;

import java.util.ArrayList;

public class ChatRoom {

    private String name;

    private ArrayList<User> participants;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<User> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }
}
