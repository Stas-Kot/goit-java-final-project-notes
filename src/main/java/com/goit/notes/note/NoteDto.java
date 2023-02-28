package com.goit.notes.note;

import com.goit.notes.user.User;
import lombok.Data;

@Data
public class NoteDto {
    private String noteId;
    private String title;
    private String content;
    private User user;
    private ENotePrivacy privacy;


    public static NoteDto fromNote(Note note) {
        NoteDto res = new NoteDto();
        res.setNoteId(note.getNoteId());
        res.setTitle(note.getTitle());
        res.setContent(note.getContent());
        res.setUser(note.getUser());
        res.setPrivacy(note.getPrivacy());
        return res;
    }

    public Note toNote(){
        Note note = new Note();
        note.setNoteId(noteId);
        note.setTitle(title);
        note.setContent(content);
        note.setUser(user);
        note.setPrivacy(privacy);
        return note;
    }
}
