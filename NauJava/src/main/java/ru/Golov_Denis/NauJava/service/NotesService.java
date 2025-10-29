package ru.Golov_Denis.NauJava.service;

import ru.Golov_Denis.NauJava.model.Note;
import java.util.List;

public interface NotesService {
    void createNote(Long id, String title, String content, List<String> tags);

    Note findById(Long id);

    void deleteById(Long id);

    void updateNoteContent(Long id, String newContent);
}
