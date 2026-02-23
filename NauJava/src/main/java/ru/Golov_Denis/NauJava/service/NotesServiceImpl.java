package ru.Golov_Denis.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.Golov_Denis.NauJava.entity.NoteEntity;
import ru.Golov_Denis.NauJava.entity.TagEntity;
import ru.Golov_Denis.NauJava.entity.NoteTagEntity;
import ru.Golov_Denis.NauJava.repository.NotesRepository;
import ru.Golov_Denis.NauJava.repository.TagRepository;
import ru.Golov_Denis.NauJava.repository.NoteTagRepository;
import ru.Golov_Denis.NauJava.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotesServiceImpl implements NotesService {

    private final NotesRepository notesRepository;
    private final TagRepository tagRepository;
    private final NoteTagRepository noteTagRepository;
    private final UserRepository userRepository;

    @Autowired
    public NotesServiceImpl(NotesRepository notesRepository, TagRepository tagRepository, NoteTagRepository noteTagRepository, UserRepository userRepository) {
        this.notesRepository = notesRepository;
        this.tagRepository = tagRepository;
        this.noteTagRepository = noteTagRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createNote(Long id, String title, String content, List<String> tags) {
        var note = new NoteEntity();
        note.setTitle(title);
        note.setContent(content);

        var user = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found"));
        note.setUser(user);

        NoteEntity saved = notesRepository.save(note);

        if (tags != null && !tags.isEmpty()) {
            List<NoteTagEntity> nts = new ArrayList<>();
            for (String tagName : tags) {
                TagEntity tag = tagRepository.findByName(tagName).orElse(null);
                if (tag == null) {
                    tag = new TagEntity();
                    tag.setName(tagName);
                    tag = tagRepository.save(tag);
                }
                NoteTagEntity nt = new NoteTagEntity(saved, tag);
                noteTagRepository.save(nt);
                nts.add(nt);
            }
            saved.setNoteTags(nts);
            notesRepository.save(saved);
        }
    }

    @Override
    public NoteEntity findById(Long id) {
        return notesRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        notesRepository.deleteById(id);
    }

    @Override
    public void updateNoteContent(Long id, String newContent) {
        NoteEntity note = notesRepository.findById(id).orElse(null);
        if (note != null) {
            note.setContent(newContent);
            notesRepository.save(note);
        }
    }
}
