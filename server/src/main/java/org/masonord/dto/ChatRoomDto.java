package org.masonord.dto;

import java.util.ArrayList;

public class ChatRoomDto {

    private String name;

    private ArrayList<UserDto> participants;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<UserDto> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<UserDto> participants) {
        this.participants = participants;
    }
}
