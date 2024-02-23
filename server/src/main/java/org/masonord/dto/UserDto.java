package org.masonord.dto;

public class UserDto {

    private String name;

    private ChatRoomDto currentRoom;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChatRoomDto getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(ChatRoomDto currentRoom) {
        this.currentRoom = currentRoom;
    }
}
