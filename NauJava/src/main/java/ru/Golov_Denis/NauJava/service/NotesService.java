package ru.Golov_Denis.NauJava.service;

import ru.Golov_Denis.NauJava.entity.NoteEntity;
import java.util.List;

public interface NotesService {
    void createNote(Long id, String title, String content, List<String> tags);
    NoteEntity findById(Long id);
    void deleteById(Long id);
    void updateNoteContent(Long id, String newContent);
}
