package org.masonord.entity;

public class User {

    private String name;

    private ChatRoom currentRoom;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChatRoom getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(ChatRoom currentRoom) {
        this.currentRoom = currentRoom;
    }
}
