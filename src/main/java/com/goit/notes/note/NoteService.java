package com.goit.notes.note;

import com.goit.notes.exceptions.NotValidNoteContentException;
import com.goit.notes.exceptions.NotValidNoteTitleException;
import com.goit.notes.security.CustomUserDetailsService;
import com.goit.notes.user.User;
import com.goit.notes.utils.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class NoteService {
    private final NoteRepository repository;
    private final Validation validation;
    private final CustomUserDetailsService customUserDetailsService;

    public List<Note> getAllByUserId(long id) {
        return repository.getAllNotesByUserId(id);
    }

    public synchronized Note add(String username, NoteDto noteDto) {
        System.out.println("\"tyt\" = " + "tyt");
        if (noteDto.getTitle() == null || !validation.isValidTitle(noteDto.getTitle())) {
            System.out.println("\"tyto\" = " + "tyto");
            throw new NotValidNoteTitleException("Not valid note title!");
        }
        if (noteDto.getContent() == null || !validation.isValidContent(noteDto.getContent())) {
            throw new NotValidNoteContentException("Not valid note content!");
        }

        Note note = new Note();
        note.setNoteId(UUID.randomUUID().toString());
        note.setPrivacy(noteDto.getPrivacy());
        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());
        User user = new User();
        user.setId(customUserDetailsService.loadUserByUsername(username).getId());
        note.setUser(user);

        return repository.save(note);
    }

    public synchronized void update(Note note) {
        if (note.getTitle() == null || !validation.isValidTitle(note.getTitle())) {
            throw new NotValidNoteTitleException("Not valid note title!");
        }
        if (note.getContent() == null || !validation.isValidContent(note.getContent())) {
            throw new NotValidNoteContentException("Not valid note content!");
        }

        Note updatedNote = getById(note.getNoteId());
        updatedNote.setContent(note.getContent());
        updatedNote.setTitle(note.getTitle());
        updatedNote.setPrivacy(note.getPrivacy());
        repository.save(updatedNote);
    }

    public Note getById(String id) {
        return repository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public synchronized void deleteById(String id) {
        repository.deleteById(id);
    }

    public Note getPublicNoteById(String id) {
        Note note = null;

        if(repository.existsById(id) && getById(id).getPrivacy().name().equals("PUBLIC")) {
            note = getById(id);
        }

        return note;
    }
}
