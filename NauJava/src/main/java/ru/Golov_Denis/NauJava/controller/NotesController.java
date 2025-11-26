package ru.Golov_Denis.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.Golov_Denis.NauJava.entity.NoteEntity;
import ru.Golov_Denis.NauJava.repository.NotesRepository;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NotesController {

    public record NoteDto(Long id, String title, String content, String username) {}

    private final NotesRepository notesRepository;

    @Autowired
    public NotesController(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    @GetMapping("/{id}")
    public NoteDto getNote(@PathVariable Long id) {
        var note = notesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id " + id));
        return convertToDto(note);
    }

    @GetMapping("/by-user/{username}")
    public List<NoteDto> getNotesByUsername(@PathVariable String username) {
        return notesRepository.findByUserUsernameCriteria(username).stream()
                .map(this::convertToDto)
                .toList();
    }

    @GetMapping("/search")
    public List<NoteDto> searchNotes(String fragment) {
        return notesRepository.findByTitleOrContentFragmentCriteria(fragment).stream()
                .map(this::convertToDto)
                .toList();
    }


    private NoteDto convertToDto(NoteEntity note) {
        return new NoteDto(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getUser() != null ? note.getUser().getUsername() : null);
    }
}
