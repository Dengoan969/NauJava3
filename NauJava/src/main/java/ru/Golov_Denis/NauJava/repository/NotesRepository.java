package ru.Golov_Denis.NauJava.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.Golov_Denis.NauJava.model.Note;

import java.util.List;

@Component
public class NotesRepository implements CrudRepository<Note, Long> {

    private final List<Note> noteContainer;

    @Autowired
    public NotesRepository(List<Note> noteContainer) {
        this.noteContainer = noteContainer;
    }

    @Override
    public void create(Note note) {
        noteContainer.add(note);
    }

    @Override
    public Note read(Long id) {
        return noteContainer.stream()
                .filter(note -> note.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(Note note) {
        for (var i = 0; i < noteContainer.size(); i++) {
            if (noteContainer.get(i).getId().equals(note.getId())) {
                noteContainer.set(i, note);
                return;
            }
        }
    }

    @Override
    public void delete(Long id) {
        noteContainer.removeIf(note -> note.getId().equals(id));
    }
}
