package com.goit.notes.note;

import com.goit.notes.exceptions.NotValidNoteContentException;
import com.goit.notes.exceptions.NotValidNoteTitleException;
import com.goit.notes.security.CustomUserDetailsService;
import com.goit.notes.security.SecurityService;
import com.goit.notes.user.UserDetailsImpl;
import com.goit.notes.user.UserService;
import com.goit.notes.user.views.EViewType;
import com.goit.notes.user.views.ViewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/note")
public class NoteController {
    private final NoteService noteService;
    private final SecurityService securityService;
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;

    @GetMapping("/example")
    public ModelAndView getExample() {
        return new ModelAndView("notes/example");
    }

    @GetMapping("/list")
    public ModelAndView getNoteList(Authentication authentication) {
        ModelAndView result = new ModelAndView("notes/list");
        String username = securityService.getUsername();
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
        String notesView = userDetails.getViewType().equals("TABLE")? "true" : "false";
        List<NoteDto> notes = noteService.getAllByUserId(userDetails.getId()).stream()
                .map(NoteDto::fromNote)
                .collect(Collectors.toList());
        result.addObject("notesView", notesView);
        result.addObject("notes", notes);
        result.addObject("username", username);
        return result;
    }

    @PostMapping("/list")
    public ModelAndView switchListTable(ViewRequest viewRequest) {
        String username = securityService.getUsername();
        if(viewRequest.getName() == null) {
            userService.setViewType(username, EViewType.LIST.name());
        } else {
            userService.setViewType(username, viewRequest.getName());
        }

        return new ModelAndView("redirect:/note/list");
    }

    @PostMapping("/delete")
    public RedirectView delete(@RequestParam(name = "noteId") String noteId) {
        noteService.deleteById(noteId);
        return new RedirectView("list");
    }

    @GetMapping("/edit")
    public ModelAndView edit(@RequestParam(name = "id") String id) {
        ModelAndView result = new ModelAndView("notes/create_edit");
        Note note = noteService.getById(id);
        String username = securityService.getUsername();
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
        String notesView = userDetails.getViewType().equals("TABLE")? "true" : "false";
        result.addObject("notesView", notesView);
        result.addObject("note", NoteDto.fromNote(note));
        result.addObject("username", username);
        return result;
    }

    @PostMapping("/edit")
    public ModelAndView saveUpdatedNote(NoteDto noteDto) {
        ModelAndView result = new ModelAndView("notes/create_edit");
        String username = securityService.getUsername();
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
        String notesView = userDetails.getViewType().equals("TABLE")? "true" : "false";
        result.addObject("notesView", notesView);
        result.addObject("note", noteDto);
        result.addObject("username", username);

        try {
            noteService.update(noteDto.toNote());
        } catch (NotValidNoteTitleException ex) {
            result.addObject("message",
                    "Note title length have to be at least 5 and not more than 100 symbols!");
            return result;
        } catch (NotValidNoteContentException ex) {
            result.addObject("message",
                    "Note content length have to be at least 5 and not more than 10000 symbols!");
            return result;
        }
        return new ModelAndView("redirect:/note/list");
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView result = new ModelAndView("notes/create_edit");
        String username = securityService.getUsername();
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
        String notesView = userDetails.getViewType().equals("TABLE")? "true" : "false";
        result.addObject("notesView", notesView);
        result.addObject("isNew", true);
        result.addObject("username", username);
        return result;
    }


    @PostMapping("/create")
    public ModelAndView saveCreatedNote(NoteDto noteDto) {
        ModelAndView result = new ModelAndView("notes/create_edit");
        String username = securityService.getUsername();
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
        String notesView = userDetails.getViewType().equals("TABLE")? "true" : "false";
        result.addObject("notesView", notesView);
        result.addObject("note", noteDto);
        result.addObject("isNew", true);
        result.addObject("username", username);

        try {
            Note saved = noteService.add(username, noteDto);
        } catch (NotValidNoteTitleException ex) {
            result.addObject("message",
                    "Note title length have to be at least 5 and not more than 100 symbols!");
            return result;
        } catch (NotValidNoteContentException ex) {
            result.addObject("message",
                    "Note content length have to be at least 5 and not more than 10000 symbols!");
            return result;
        }

        return new ModelAndView("redirect:/note/list");
    }

    @GetMapping("/share/{id}")
    public ModelAndView info(@PathVariable("id") String id) {
        ModelAndView result = new ModelAndView("notes/note");
        Note note = noteService.getPublicNoteById(id);
        if(note != null) {
            return result.addObject("note", note);
        } else {
            return result.addObject("message", "This note does not exist!:(");
        }
    }
}
