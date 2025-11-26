package ru.Golov_Denis.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.Golov_Denis.NauJava.entity.NoteEntity;
import ru.Golov_Denis.NauJava.repository.NotesRepository;

import java.util.List;

@Controller
public class NotesWebController {

    private final NotesRepository notesRepository;

    @Autowired
    public NotesWebController(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    @GetMapping("/notes")
    public String getNotesPage(Model model) {
        List<NoteEntity> notes = (List<NoteEntity>) notesRepository.findAll();
        model.addAttribute("notes", notes);
        return "notes";
    }
}
