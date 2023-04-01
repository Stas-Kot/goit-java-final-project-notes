package com.goit.notes.user;


import com.goit.notes.note.Note;
import com.goit.notes.user.views.EViewType;
import lombok.Data;

import java.util.List;


@Data
public class UserDto {
    private Long id;
    private String name;
    private EUserRole role;
    private EViewType viewType;
    private List<Note> notes;

    public static UserDto fromUser(User user) {
        UserDto res = new UserDto();
        res.setId(user.getId());
        res.setName(user.getUsername());
        res.setRole(user.getRole());
        res.setNotes(user.getNotes());
        res.setViewType(user.getViewType());
        return res;
    }

    public User toUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getName());
        user.setRole(userDto.getRole());
        user.setNotes(userDto.getNotes());
        user.setViewType(userDto.getViewType());
        return user;
    }
}
