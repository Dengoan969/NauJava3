package ru.Golov_Denis.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.Golov_Denis.NauJava.model.Note;
import ru.Golov_Denis.NauJava.repository.NotesRepository;

import java.util.List;

@Service
public class NotesServiceImpl implements NotesService {

    private final NotesRepository notesRepository;

    @Autowired
    public NotesServiceImpl(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    @Override
    public void createNote(Long id, String title, String content, List<String> tags) {
        var note = new Note(id, title, content, tags);
        notesRepository.create(note);
    }

    @Override
    public Note findById(Long id) {
        return notesRepository.read(id);
    }

    @Override
    public void deleteById(Long id) {
        notesRepository.delete(id);
    }

    @Override
    public void updateNoteContent(Long id, String newContent) {
        var note = notesRepository.read(id);

        if (note != null) {
            note.setContent(newContent);
            notesRepository.update(note);
        }
    }
}
